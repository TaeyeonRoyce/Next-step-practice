package webserver;

import java.util.HashMap;
import java.util.Map;

import webserver.controller.Controller;
import webserver.controller.CreateUserController;
import webserver.controller.ListController;
import webserver.controller.LoginController;

public class ControllerMapping {

	private static Map<String, Controller> controllerMap = new HashMap<>();

	static {
		controllerMap.put("/user/create", new CreateUserController());
		controllerMap.put("/user/login", new LoginController());
		controllerMap.put("/user/list.html", new ListController());
	}

	public static Controller getControllerByPath(String path) {
		return controllerMap.get(path);
	}


}
