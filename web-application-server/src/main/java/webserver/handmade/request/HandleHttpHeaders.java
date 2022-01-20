package webserver.handmade.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleHttpHeaders {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private Map<String, String> headers = new HashMap<>();

	public HandleHttpHeaders(BufferedReader br) throws IOException {
		handleHttpHeaders(br);
	}

	private void handleHttpHeaders(BufferedReader br) throws IOException {
		String line = br.readLine();
		while (!line.equals("")) {
			String[] splitLine = line.split(":");
			this.headers.put(splitLine[0].trim(), splitLine[1].trim());
			line = br.readLine();
		}
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}
