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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8")); //http header
            DataOutputStream dos = new DataOutputStream(out);

            String requestLine = br.readLine();
            log.debug("request line : {}", requestLine);

            HttpMethod httpMethod = extractMethodFromRequest(requestLine);
            String requestURI = getRequestURI(requestLine);

            //HttpMethod와 URI로 mapping하기
            MethodMapping methodMapping = new MethodMapping(httpMethod, requestURI);

            //mapping이후 응답
            String response = methodMapping.mapping(br);

            Path URIPath = new File("./webapp" + response).toPath();

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



    private HttpMethod extractMethodFromRequest(String request) {
        String s = request.split(" ")[0];
        return HttpMethod.getMethodByString(s);
    }

    private String getRequestURI(String requestLine) {
        return requestLine.split(" ")[1];
    }
}
