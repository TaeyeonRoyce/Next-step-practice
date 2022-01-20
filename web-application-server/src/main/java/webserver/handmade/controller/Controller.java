package webserver.handmade.controller;

import webserver.handmade.request.HttpRequest;
import webserver.handmade.response.HttpResponse;

public interface Controller {
	void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
