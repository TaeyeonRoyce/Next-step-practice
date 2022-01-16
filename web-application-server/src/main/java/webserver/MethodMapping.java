package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpMethod;
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

	public String postMapping(BufferedReader br) throws IOException {
		if (URI.equals("/user/create")) {
			return userSignUp(br);
		} else if (URI.equals("/user/login")) {
			return userSignIn(br);
		}
		return null;
	}

	private String userSignUp(BufferedReader br) throws IOException {
		String body = getBody(br);

		Map<String, String> params = MyHttpRequestUtils.parseQueryString(body);
		User userByParams = MyHttpRequestUtils.createUserByParams(params);
		DataBase.addUser(userByParams);
		log.debug("User : {}", userByParams);

		return "/index.html"; //회원 가입이 완료되면 index.html로 이동(redirect)
	}

	private int getContentLength(String singleLine) {
		String[] headerTokens = singleLine.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}

	private String userSignIn(BufferedReader br) throws IOException {
		String body = getBody(br);

		Map<String, String> params = MyHttpRequestUtils.parseQueryString(body);
		User user = DataBase.findUserById(params.get("userId"));
		if (user == null) {
			return "/user/login_failed.html";
		}

		if (user.getPassword().equals(params.get("password"))) {
			log.debug("login Success by : {}", user.getUserId());
			return "SING_IN";
		} else {
			return "/user/login_failed.html";
		}


	}

	private String getBody(BufferedReader br) throws IOException {
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
		return body;
	}

}
