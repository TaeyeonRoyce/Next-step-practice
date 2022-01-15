# 회원가입(POST)

### 기존 회원가입 방식(GET)의 문제점을 극복하기 위해 Http에서 지원하는 POST메서드를 활용해보자

### 해결 아이디어

1. POST메서드에서는 URI로부터 Query를 받아올 수 없다.
2. Body를 직접 조회해서 회원가입 정보를 분석 및 추출할 것.
3. 제공된 `IOUtils.readData`를 활용하여 추출
4. `User`가입하기



### 1. Body로부터 클라이언트 요청에 담긴 정보 추출하기

우선, `/form.html`의 `회원가입` `<form>` 태그의 메서드를 `POST`로 변경한다.

```html
<form name="question" method="post" action="/user/create">
```

위 HTML태그는 자손의 행동이(button 클릭) `/user/create`라는 URI로 Post 메서드로 보내고 있음을 알 수 있다.

로깅을 해보면,

```
17:01:42.356 [DEBUG] [Thread-12] [webserver.RequestHandler] - request line : POST /user/create HTTP/1.1
```

보이는 것 처럼 GET방식과 다르게 URI에 Data가 노출되지 않아 보안적인 부분에서 조금 더 안전하긴 하다.

하지만, 기존 방식으로는 원하는 정보를 추출할 수 없기 때문에, 제공된 `.readData()`를 활용해야 한다.

### 2. IOUtils.readData()

```java
/**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
public static String readData(BufferedReader br, int contentLength) throws IOException {
  char[] body = new char[contentLength];
  br.read(body, 0, contentLength);
  return String.copyValueOf(body);
}
```

http헤더의 Content-Length를 알면, body내에 있는 원하는 정보를 추출할 수 있다.



http메세지를 출력해보면, 다음과 같은 긴 정보들을 얻을 수 있다.

```
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - request line : POST /user/create HTTP/1.1
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Origin: http://localhost:8080
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Content-Type: application/x-www-form-urlencoded
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Accept-Encoding: gzip, deflate
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Connection: keep-alive
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Upgrade-Insecure-Requests: 1
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Referer: http://localhost:8080/user/form.html
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Content-Length: 59
17:31:19.237 [DEBUG] [Thread-17] [webserver.RequestHandler] - Accept-Language: en-us
17:35:50.218 [DEBUG] [Thread-0] [webserver.RequestHandler] - body: userId=Royce&password=1q2w3e4r&name=Won&email=aaa%40gmail.com
```

```java
public String userSignIn(BufferedReader br) throws IOException {
		String singleLine = br.readLine();
		int contentLength = 0;
		while (!singleLine.equals("")) {
			singleLine = br.readLine();
			if (singleLine.contains("Content-Length")) {
				contentLength = getContentLength(singleLine);
				break;
			}
		}
  	String body = IOUtils.readData(br, contentLength);
		log.debug("body: {}", body);
  //17:35:50.218 [DEBUG] [Thread-0] [webserver.RequestHandler] - body: userId=Royce&password=1q2w3e4r&name=Won&email=aaa%40gmail.com

		Map<String, String> params = MyHttpRequestUtils.parseQueryString(body);
		User userByParams = MyHttpRequestUtils.createUserByParams(params);
		log.debug("User : {}", userByParams);

		return "/index.html"; //회원 가입이 완료되면 index.html로 이동(redirect)
  
	}

private int getContentLength(String singleLine) throws IOException {
  String[] headerTokens = singleLine.split(":");
  return Integer.parseInt(headerTokens[1].trim());
}
```

`BufferedReader`로 긴 메세지를 돌면서 `Content-Length`헤더를 만나면, 그 값을 찾아내고, 이를 활용해서 `IOUtils.readData()`의 파라미터로 넘겨주어 body를 찾아낸다.

찾아낸 body에 담긴 `query`를 통하여 기존 방식과 동일하게 `User`를 저장한다.



### 문제점

POST메소드를 통해 가입에 대한 요청을 GET보다 더욱 개선하여 응답하였다. 하지만, 회원가입이 완료된 후 메인 홈페이지(index.html)로 돌아가는 로직에 문제점이 존재한다.

만약 위 방식으로 메인 페이지로 돌려보낸뒤, 클라이언트에서 새로고침을 한다면 어떤 일이 벌어질까? 브라우저에서 새로고침은 요청을 다시 보내는 것이다. 다시 말해, 보이는 화면은 `index.html`이지만 가지고 있는 요청이 아직 `Post /user/create ...`이라는 것이다. 화면과 요청의 불일치는 오류를 발생시키기 쉽다.

이러한 경우는 Http 상태코드를 통해 해결할 수 있다.(redirect: 3XX)