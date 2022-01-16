package util;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import model.User;

public class MyHttpRequestUtilsTest {

	@Test
	void extractQueryTest() {
		//given
		String query = "userId=Royce&password=121121&name=태연&email=royce@gmail.com";

		//when
		String uri = "/user/create?userId=Royce&password=121121&name=태연&email=royce@gmail.com";

		//then
		Assertions.assertEquals(MyHttpRequestUtils.extractQueryFromURI(uri), query);
	}

	@Test
	void parseQueryStringTest() {
		//given
		String query = "userId=Royce&password=121121&name=태연&email=royce@gmail.com";

		//when
		Map<String, String> params = MyHttpRequestUtils.parseQueryString(query);

		//then
		Assertions.assertEquals(params.get("userId"), "Royce");
		Assertions.assertEquals(params.get("password"), "121121");
		Assertions.assertEquals(params.get("name"), "태연");
		Assertions.assertEquals(params.get("email"), "royce@gmail.com");
	}

	@Test
	void createUserTest() {
		//given
		String query = "userId=Royce&password=121121&name=태연&email=royce@gmail.com";

		//when
		Map<String, String> params = MyHttpRequestUtils.parseQueryString(query);
		User userByParams = MyHttpRequestUtils.createUserByParams(params);

		//then
		Assertions.assertEquals(userByParams.getName(), "태연");
		Assertions.assertEquals(userByParams.getPassword(), "121121");
	}

	@Test
	void parseCookieTest() {
		//given
		String cookies = "Cookie: login=true;";

		//when
		Map<String, String> parseCookies = MyHttpRequestUtils.parseCookies(cookies);
		for(String key : parseCookies.keySet() ){
			String value = parseCookies.get(key);
			System.out.println(String.format("키 : "+key+", 값 : "+value));
		}
		//then
		String login = parseCookies.get("login");
		Assertions.assertEquals(login, "true");
	}

}