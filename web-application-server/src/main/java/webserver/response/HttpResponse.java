package webserver.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

	public static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private DataOutputStream dos;
	private Map<String, String> headers = new HashMap<>();

	public HttpResponse(OutputStream dos) {
		this.dos = new DataOutputStream(dos);
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public void forward(String path) {
		try {
			byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

			if (path.endsWith(".css")) {
				headers.put("Content-Type", "text/css");
			} else if (path.endsWith(".js")) {
				headers.put("Content-Type", "application/javascript");
			} else {
				headers.put("Content-Type", "text/html;charset=utf-8");
			}
			headers.put("Content-Length", body.length + "");
			response200Header(body.length);
			responseBody(body);
		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}

	public void forwardBody(String body) {
		byte[] contents = body.getBytes();
		headers.put("Content-Type", "text/html;charset=utf-8");
		headers.put("Content-Length", contents.length + "");
		response200Header(contents.length);
		responseBody(contents);
	}

	public void sendRedirect(String redirectPath) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			processHeader();
			dos.writeBytes("Location: " + redirectPath + " \r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void processHeader() {
		try {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
			}
		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}


	private void response200Header(int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			processHeader();
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
