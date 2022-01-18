package webserver.controller;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class ListController extends AbstractController {

	//path = "/user/list"

	private static final Logger log = LoggerFactory.getLogger(ListController.class);
	public static final String USER_LOGIN = "/user/login.html";

	@Override
	public void get(HttpRequest httpRequest, HttpResponse httpResponse) {
		String cookie = httpRequest.getHeader("Cookie");
		boolean isLogin = findLoginByCookie(cookie);

		log.debug("Logined : {}", isLogin);
		if (isLogin) {
			httpResponse.forwardBody(createUserListTable());
		} else {
			httpResponse.forward(USER_LOGIN);
		}
	}

	public String createUserListTable() {
		Collection<User> users = DataBase.findAll();
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='1'>");
		for (User user : users) {
			sb.append("<tr>");
			sb.append("<td>" + user.getUserId() + "</td>");
			sb.append("<td>" + user.getName() + "</td>");
			sb.append("<td>" + user.getEmail() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	private boolean findLoginByCookie(String line) {
		Map<String, String> parseCookies = HttpRequestUtils.parseCookies(line);
		String login = parseCookies.get("login");
		if (login == null) {
			return false;
		}
		log.debug("login : {}", login);

		return Boolean.parseBoolean(login);
	}
}
