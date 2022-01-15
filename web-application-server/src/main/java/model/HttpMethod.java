package model;

public enum HttpMethod {
	GET,
	POST;

	public static HttpMethod getMethodByString(String method) {
		if (method.equals("GET")) {
			return GET;
		} else if (method.equals("POST")) {
			return POST;
		}
		return null;
	}
}
