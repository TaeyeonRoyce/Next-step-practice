package webserver.handmade.request;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HttpMethod;
import util.HttpRequestUtils;

public class HandleRequestLine {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private HttpMethod method;
	private String path;
	private Map<String, String> params = new HashMap<>();

	public HandleRequestLine(String line) {
		handleRequestLine(line);
	}

	private void handleRequestLine(String line) {
		log.debug("request line : {}", line);
		String[] splitLine = line.split(" ");
		this.method = HttpMethod.getMethodByString(splitLine[0]);

		String requestPath = splitLine[1];
		if (this.method == HttpMethod.POST) {
			this.path = requestPath;
			return;
		}
		findQueryStringFromPath(requestPath);
	}

	private void findQueryStringFromPath(String path) {

		String[] splitPath = path.split("\\?");
		this.path = splitPath[0];
		if (splitPath.length > 1) {
			this.params = HttpRequestUtils.parseQueryString(splitPath[1]);
		}
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getParams() {
		return params;
	}
}
