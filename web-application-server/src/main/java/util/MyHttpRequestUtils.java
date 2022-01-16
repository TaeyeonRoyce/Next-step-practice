package util;

import java.util.HashMap;
import java.util.Map;

import model.User;

public class MyHttpRequestUtils {

	public static String extractQueryFromURI(String URI) {
		String queryStartChar = "\\?";
		String[] split = URI.split(queryStartChar);
		return split[1];
	}

	//userId={userId}&password={password}&name={name}&email={email}
	public static Map<String, String> parseQueryString(String queryString) {
		Map<String, String> parseMap = new HashMap<>();
		String[] params = queryString.split("&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			parseMap.put(keyValue[0], keyValue[1]);
		}

		return parseMap;
	}

	public static User createUserByParams(Map<String, String> params) {
		return new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
	}


	//Cookie: login=true;
	public static Map<String, String> parseCookies(String cookiesLine) {
		Map<String, String> parseMap = new HashMap<>();

		String[] cookies = cookiesLine.split(";|:");
		for (int i = 1; i < cookies.length; i++) {
			String[] keyValue = cookies[i].split("=");
			parseMap.put(keyValue[0].trim(), keyValue[1].trim());
		}
		return parseMap;
	}


}
