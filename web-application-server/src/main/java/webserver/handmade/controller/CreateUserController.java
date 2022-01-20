package webserver.handmade.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.handmade.request.HttpRequest;
import webserver.handmade.response.HttpResponse;

public class CreateUserController extends AbstractController {
	//path = "user/create"

	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@Override
	public void post(HttpRequest httpRequest, HttpResponse httpResponse) {
		User user = new User(
			httpRequest.getParam("userId"),
			httpRequest.getParam("password"),
			httpRequest.getParam("name"),
			httpRequest.getParam("email"));

		log.debug("User : {}", user);
		DataBase.addUser(user);
		httpResponse.sendRedirect("/index.html");
	}

	@Override
	public void get(HttpRequest httpRequest, HttpResponse httpResponse) {
		httpResponse.sendRedirect("/index.html");
	}
}
