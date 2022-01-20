package webserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import webserver.handmade.response.HttpResponse;

public class HttpResponseTest {
	private String testDirectory = "./src/test/resources";

	@Test
	void responseForward() throws FileNotFoundException {
		OutputStream outputStream = new FileOutputStream(testDirectory + "Http_Forward.txt");
		HttpResponse response = new HttpResponse(outputStream);

		response.forward("/index.html");
	}

	@Test
	void responseRedirect() throws FileNotFoundException {
		OutputStream outputStream = new FileOutputStream(testDirectory + "Http_Redirect.txt");
		HttpResponse response = new HttpResponse(outputStream);

		response.sendRedirect("/index.html");
	}

	@Test
	void responseCookies() throws FileNotFoundException {
		OutputStream outputStream = new FileOutputStream(testDirectory + "Http_Cookie.txt");
		HttpResponse response = new HttpResponse(outputStream);

		response.addHeader("Set-Cookie", "login=true");
		response.sendRedirect("/index.html");
	}
}
