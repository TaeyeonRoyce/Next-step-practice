package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {
	private String testDirectory = "./src/test/resources";

	@Test
	void responseForward() {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_Forward.txt"));
		HttpResponse response = new HttpResponse(in);

		response.forward("/index.html");
	}

	@Test
	void responseRedirect() {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_Redirect.txt"));
		HttpResponse response = new HttpResponse(in);

		response.forward("/index.html");
	}

	@Test
	void responseCookies() {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_Cookie.txt"));
		HttpResponse response = new HttpResponse(in);

		response.addHeader("Set-Cookie", "login=true");
		response.forward("/index.html");
	}
}
