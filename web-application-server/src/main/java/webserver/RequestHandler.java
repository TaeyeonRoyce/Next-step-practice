package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HttpMethod;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        // log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        //         connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8")); //http header
            DataOutputStream dos = new DataOutputStream(out);

            String requestLine = br.readLine();


            HttpMethod httpMethod = extractMethodFromRequest(requestLine);
            String requestURI = getRequestURI(requestLine);
            log.debug("httpMethod : {}", httpMethod);
            log.debug("requestURI : {}", requestURI);

            MethodMapping methodMapping = new MethodMapping(httpMethod, requestURI);

            //post Mapping 후 redirectURI로 redirect하기
            if (httpMethod == HttpMethod.POST) {
                //postMapping 메서드는 해당 URI에 대한 책임 수행 후 리다이렉트 할 URI반환
                String redirectURI = methodMapping.postMapping(br);

                if (redirectURI.equals("SING_IN")) {
                    response302LoginSuccessHeader(dos);
                    return;
                } else if (redirectURI.equals("/user/login_failed.html")) {
                    response302Header(dos, redirectURI);
                    log.debug("login failed");
                    return;
                } else {
                    //302코드 반환
                    response302Header(dos, redirectURI);
                }
            } else if (httpMethod == HttpMethod.GET && requestURI.equals("/user/list.html")) {
                boolean login = methodMapping.isLogin(br);
                if (!login) {
                    responseResource(out, "/user/login.html");
                    return;
                }
                byte[] body = methodMapping.createUserListTable().getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else if (httpMethod == HttpMethod.GET && requestURI.endsWith(".css")) {
                byte[] body = Files.readAllBytes(new File("./webapp" + requestURI).toPath());
                response200CssHeader(dos, body.length);
                responseBody(dos, body);
            }

            responseResource(out, requestURI);

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


    private void response302Header(DataOutputStream dos, String URI) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + URI + " \r\n");
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

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Set-Cookie: login=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-type: text/css\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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

    private void responseResource(OutputStream out, String URI) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        Path URIPath = new File("./webapp" + URI).toPath();
        byte[] body = Files.readAllBytes(URIPath);

        response200Header(dos, body.length);
        responseBody(dos, body);
    }
}
