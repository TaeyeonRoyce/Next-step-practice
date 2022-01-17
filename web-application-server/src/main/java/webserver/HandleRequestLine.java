package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.MyHttpRequestUtils;

public class HandleRequestLine {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private String method;
	private String path;
	private Map<String, String> params = new HashMap<>();

	public HandleRequestLine(String line) {
		handleRequestLine(line);
	}

	private void handleRequestLine(String line) {
		log.debug("request line : {}", line);
		String[] splitLine = line.split(" ");
		this.method = splitLine[0];

		String requestPath = splitLine[1];
		if (this.method.equals("POST")) {
			this.path = requestPath;
			return;
		}
		findQueryStringFromPath(requestPath);
	}

	private void findQueryStringFromPath(String path) {
		String[] splitPath = path.split("\\?");
		this.path = splitPath[0];
		this.params = MyHttpRequestUtils.parseQueryString(splitPath[1]);
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getParams() {
		return params;
	}
}
