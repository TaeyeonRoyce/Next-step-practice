package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.IOUtils;
import util.MyHttpRequestUtils;

public class MethodMapping {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private HttpMethod httpMethod;
	private String URI;

	public MethodMapping(HttpMethod httpMethod, String URI) {
		this.httpMethod = httpMethod;
		this.URI = URI;
	}

	public String mapping(BufferedReader br) throws IOException {
		if (httpMethod == HttpMethod.GET) {
			return URI;
		} else if (httpMethod == HttpMethod.POST) {
			if (URI.equals("/user/create")) {
				return userSignIn(br);
			}
		}
		return null;
	}

	private String userSignIn(BufferedReader br) throws IOException {
		String singleLine = br.readLine();
		int contentLength = 0;
		while (!singleLine.equals("")) {
			singleLine = br.readLine();
			if (singleLine.contains("Content-Length")) {
				contentLength = getContentLength(singleLine);
			}
		}

		String body = IOUtils.readData(br, contentLength);
		log.debug("body: {}", body);

		Map<String, String> params = MyHttpRequestUtils.parseQueryString(body);
		User userByParams = MyHttpRequestUtils.createUserByParams(params);
		log.debug("User : {}", userByParams);

		return "/index.html"; //회원 가입이 완료되면 index.html로 이동(redirect)
	}

	private int getContentLength(String singleLine) throws IOException {
		String[] headerTokens = singleLine.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}
}
