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

}
