package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class LoginController extends AbstractController {
	//path = "/user/login.html"

	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);
	public static final String LOGIN_FAILED_HTML = "/user/login_failed.html";

	@Override
	public void post(HttpRequest httpRequest, HttpResponse httpResponse) {
		User user = DataBase.findUserById(httpRequest.getParam("userId"));
		if (user == null) {
			httpResponse.sendRedirect(LOGIN_FAILED_HTML);
		}
		if (user.getPassword().equals(httpRequest.getParam("password"))) {
			log.debug("login Success by : {}", user.getUserId());
			httpResponse.addHeader("Set-Cookie", "login=true");
			httpResponse.sendRedirect("/index.html");
		} else {
			httpResponse.sendRedirect(LOGIN_FAILED_HTML);
		}
	}

	@Override
	public void get(HttpRequest httpRequest, HttpResponse httpResponse) {
		httpResponse.forward("/user/login.html");
	}
}
