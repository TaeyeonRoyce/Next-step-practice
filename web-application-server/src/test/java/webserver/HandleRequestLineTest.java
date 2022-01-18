package webserver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.HttpMethod;

public class HandleRequestLineTest {

	@Test
	void requestLineGETTest() {
		//given
		String requestLine = "GET /user/create?userId=Royce&password=password&name=Taeyeon&email=royce@gmail.com HTTP/1.1";

		//when
		HandleRequestLine hr = new HandleRequestLine(requestLine);

		//then
		assertEquals(hr.getMethod(), HttpMethod.GET);
		assertEquals(hr.getPath(), "/user/create");
		assertEquals(hr.getParams().get("name"), "Taeyeon");
	}

	@Test
	void requestLinePOSTTest() {
		//given
		String requestLine = "POST /user/create HTTP/1.1";

		//when
		HandleRequestLine hr = new HandleRequestLine(requestLine);

		//then
		assertEquals(hr.getMethod(), HttpMethod.POST);
		assertEquals(hr.getPath(), "/user/create");
	}
}