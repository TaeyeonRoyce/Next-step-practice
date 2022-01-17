package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private String method;
	private String path;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> params = new HashMap<>();

	public HttpRequest(InputStream inputStream) {
		saveFieldByInputStream(inputStream);
	}

	private void saveFieldByInputStream(InputStream inputStream) {
		try {
			BufferedReader br = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String line = br.readLine();
			if (line == null) {
				return;
			}

			handleRequestLine(line);
			handleHttpHeaders(br);

			if (this.method.equals("POST")) {
				String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				parseQueryString(body);
			}

		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
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
		parseQueryString(splitPath[1]);

	}

	private void parseQueryString(String queryString) {
		String[] splitQueryString = queryString.split("&");
		for (String params : splitQueryString) {
			String[] splitParams = params.split("=");
			this.params.put(splitParams[0].trim(), splitParams[1].trim());
		}
	}

	private void handleHttpHeaders(BufferedReader br) throws IOException {
		String line = br.readLine();
		while (!line.equals("")) {
			log.debug("header : {}", line);
			String[] splitLine = line.split(":");
			this.headers.put(splitLine[0].trim(), splitLine[1].trim());
			line = br.readLine();
		}
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getHeader(String keyString) {
		return headers.get(keyString);
	}

	public String getParam(String keyString) {
		return params.get(keyString);
	}
}
