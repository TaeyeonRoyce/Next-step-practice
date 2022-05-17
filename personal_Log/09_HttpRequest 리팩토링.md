# HttpRequest 리팩토링

기존 작성한 `HttpRequest`의 다음과 같은 로직들에 대한 문제를 발견 할 수 있다.

```java
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
```

`handleRequestLine()`메서드는 Http요청의 메서드와 경로를 찾아주고, query가 포함된 경우 이를 분석하여 저장해주는 역할을 수행하고 있다.

위 메서드는 이전 메서드인 `saveFieldByInputStream()` 라는 메서드에 의해 호출되므로 접근 범위가 `private`이다.  `RequestLine`을 처리하는 메인 로직이라고 할 수 있는 이 메서드를 테스트 하기 위해선 어떻게 해야할까?

클래스를 분리하여 리팩토링을 진행하였다.

`HandleReqestLine`이라는 클래스를 새로 생성하여 다음과 같은 책임을 부여하였다.

- `RequestLine`을 분석하여 HTTP.method, 경로 및 쿼리문 파악하기

  ```java
  package webserver;
  
  import java.util.HashMap;
  import java.util.Map;
  
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  
  import util.MyHttpRequestUtils;import webserver.handmade.request.HttpRequest;
  
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
  ```

이렇게 역할을 분리함으로써 Test코드도 작성이 가능해진다.

```java
public class HandleRequestLineTest {

	@Test
	void requestLineGETTest() {
		//given
		String requestLine = "GET /user/create?userId=Royce&password=password&name=Taeyeon&email=royce@gmail.com HTTP/1.1";

		//when
		HandleRequestLine hr = new HandleRequestLine(requestLine);

		//then
		assertEquals(hr.getMethod(), "GET");
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
		assertEquals(hr.getMethod(), "POST");
		assertEquals(hr.getPath(), "/user/create");
	}
}
```

`HttpRequest`의 역할을 분리하였기 때문에, `HttpRequest`의 무게도 줄일 수 있다.

### 동일한 방식으로, 다음과 같은 로직을 분리해내었다.

```java
private void handleHttpHeaders(BufferedReader br) throws IOException {
  String line = br.readLine();
  while (!line.equals("")) {
    log.debug("header : {}", line);
    String[] splitLine = line.split(":");
    this.headers.put(splitLine[0].trim(), splitLine[1].trim());
    line = br.readLine();
  }
}
```

`HandleHttpHeaders`이라는 클래스를 새로 생성하여 다음과 같은 책임을 부여하였다.

- `HttpHeaders`를 분석하여 `Map<String, String>`으로 저장하기

  ```java
  public class HandleHttpHeaders {
  	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
  
  	private Map<String, String> headers = new HashMap<>();
  
  	public HandleHttpHeaders(BufferedReader br) throws IOException {
  		handleHttpHeaders(br);
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
  
  	public Map<String, String> getHeaders() {
  		return headers;
  	}
  }
  ```

  

### HttpRequest 리팩토링 결과

```java
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

```

해당 클래스의 무게가 눈에 띄게 줄어들었다. 추가로 필드변수들도 줄어들었는데, 역할을 분리하여 가져간 클래스들이 필요한 필드또한 분리하여 가져갔기 때문이다.

`getXXX()`메서드를 보면 해당 역할을 수행하는 클래스로부터 데이터를 받아오는 것을 알 수 있다.

### HttpMethod - Enum으로 분류

GET, POST메소드에 대해서 `.equals("GET")`, `.eqauls("POST")`처럼 하드코딩하여 구분하였던 부분을 Enum을 활용하여 리팩토링 하였다.

```java
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
```

