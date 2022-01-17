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
import util.MyHttpRequestUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private Map<String, String> params = new HashMap<>();

	private HandleRequestLine handleRequestLine;
	private HandleHttpHeaders handleHttpHeaders;
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

			handleRequestLine = new HandleRequestLine(line);
			handleHttpHeaders = new HandleHttpHeaders(br);

			if (getMethod().equals("POST")) {
				String body = IOUtils.readData(br, Integer.parseInt(getHeader("Content-Length")));
				this.params = MyHttpRequestUtils.parseQueryString(body);
			} else {
				this.params = handleRequestLine.getParams();
			}

		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}

	private Map<String, String> getHeadersFromHandler() {
		return handleHttpHeaders.getHeaders();
	}

	public String getMethod() {
		return handleRequestLine.getMethod();
	}

	public String getPath() {
		return handleRequestLine.getPath();
	}

	public String getHeader(String keyString) {
		return getHeadersFromHandler().get(keyString);
	}

	public String getParam(String keyString) {
		return params.get(keyString);
	}
}
