package webserver;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import webserver.handmade.request.HttpRequest;

public class HttpRequestTest {
	private String testDirectory = "./src/test/resources/";

	@Test
	void request_GET() throws Exception {
		InputStream in = new FileInputStream(testDirectory + "Http_GET.txt");
		HttpRequest request = new HttpRequest(in);

		Assertions.assertEquals("GET", request.getMethod());
		Assertions.assertEquals("/user/create", request.getPath());
		Assertions.assertEquals("keep-alive", request.getHeader("Connection"));
		Assertions.assertEquals("Royce", request.getParam("userId"));

	}

	@Test
	void request_POST() throws Exception {
		InputStream in = new FileInputStream(testDirectory + "Http_POST.txt");
		HttpRequest request = new HttpRequest(in);

		Assertions.assertEquals("POST", request.getMethod());
		Assertions.assertEquals("/user/create", request.getPath());
		Assertions.assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-type"));
		Assertions.assertEquals("Royce", request.getParam("userId"));

	}

}
