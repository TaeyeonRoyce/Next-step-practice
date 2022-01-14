package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.IOUtils;
import util.MyHttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String brLine = br.readLine();
            int contentLength = 0;
            while (!brLine.equals("")) {
                log.debug("header : {}", brLine);
                brLine = br.readLine();
                if (brLine.contains("Content-Length")) {
                    contentLength = getContentLength(brLine);
                }
            }
            log.debug("request line : {}", brLine);
            DataOutputStream dos = new DataOutputStream(out);
            // byte[] body = "Hello World".getBytes();

            String[] httpRequestLine = brLine.split(" "); //URI를 추출하기 위해 배열로 변환
            String requestURI = httpRequestLine[1];
            log.debug("request URI : {}", requestURI);

            if (requestURI.equals("/user/create")) {
                log.debug("contentLength : {}", contentLength);
                String body = IOUtils.readData(br, contentLength);

                // String queryString = MyHttpRequestUtils.extractQueryFromURI(requestURI);
                log.debug("body: {}", body);

                // Map<String, String> params =
                //     HttpRequestUtils.parseQueryString(queryString);

                Map<String, String> params = MyHttpRequestUtils.parseQueryString(body);
                User userByParams = MyHttpRequestUtils.createUserByParams(params);
                log.debug("User : {}", userByParams);

                requestURI = "/index.html"; //회원 가입이 완료되면 index.html로 이동(redirect)
            }

            Path URIPath = new File("./webapp" + requestURI).toPath();

            byte[] body = Files.readAllBytes(URIPath);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String brLine) {
        String[] headerTokens = brLine.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }
}
