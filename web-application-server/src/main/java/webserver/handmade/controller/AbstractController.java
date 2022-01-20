package webserver.handmade.controller;

import model.HttpMethod;
import webserver.handmade.request.HttpRequest;
import webserver.handmade.response.HttpResponse;

abstract public class AbstractController implements Controller {

	@Override
	public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
		if (httpRequest.getMethod() == HttpMethod.GET) {
			get(httpRequest, httpResponse);
		} else if (httpRequest.getMethod() == HttpMethod.POST) {
			post(httpRequest, httpResponse);
		}
	}

	protected void get(HttpRequest httpRequest, HttpResponse httpResponse) {}
	protected void post(HttpRequest httpRequest, HttpResponse httpResponse) {}

}
