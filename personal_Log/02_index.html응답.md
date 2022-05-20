# index.html 응답하기

요구사항 1을 충족하기 위해선,

### http://localhost:8080/index.html이라는 요청에 대해 index.html을 응답하여야 한다.

주어진 소스코드 분석에 따르면, 프로그램이 실행될 경우 `WebServer`라는 클래스에서 `ServerSocket`을 활용하여 8080포트를 사용하여 웹서버를 수행하도록 되어있다. `Socket`을 통해 연결을 한 뒤 요청을 기다린다.

이 상태에서 웹서버로 요청이 들어오면, `RequestHandler`에서 클라이언트의 IP와 Port를 로깅한뒤  "Hello World"를 반환하는 과정을 거치고 있다.

### 해결 아이디어

1. 클라이언트의 요청의 URI가 `index.html`인지 확인.
2. `index.html`이라면 리소스 디렉토리에 있는 index.html 반환.



### 1. indxe.html 확인하기

클라이언트의 요청은 브라우저에서 http로 전달될 것이다.

주소창에 `http://localhost:8080/index.html`을 입력하면, 다음과 같은 요청을 확인할 수 있다.

```
Request
GET /index.html HTTP/1.1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Upgrade-Insecure-Requests: 1
Host: localhost:8080
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15
Accept-Language: en-us
Accept-Encoding: gzip, deflate
Connection: keep-alive
```

요청에 대해 다음과 같은 정보를 확인할 수 있다.

`GET /index.html HTTP/1.1` 요청라인

`...Host: localhost:8080...` 요청 헤더

위 요청정보에서 localhost:8080/index.html을 검증하도록 하여야 한다.

그러기 위해선 http요청에 대해서 요청 라인으로부터 URI(index.html)를 추출해야한다.

```java
try (InputStream in = connection.getInputStream(); 
     OutputStream out = connection.getOutputStream()) {
  BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
  String brLine = br.readLine();
  if (brLine == null) {
    return;
  }
  log.debug("request line : {}", brLine);
  /*
  15:25:02.717 [DEBUG] [Thread-0] [webserver.handmade.RequestHandler] - request line : GET /index.html HTTP/1.1
  */
}
```

`BufferedReader.readLine()`을 통해 `InputStream`으로 받아온 http요청으로부터 한 줄 추출할 수 있다.

이 한 줄로부터 URI에 해당하는 `index.html`을 추출해보자. 문자열을 조작하는 연습은 많이 해왔기 때문에 어렵지 않을 것 같다.

```java
String[] httpRequestLine = brLine.split(" "); //URI를 추출하기 위해 배열로 변환
String requestUri = httpRequestLine[1];
log.debug("request uri : {}", requestUri);
/*
15:40:03.692 [DEBUG] [Thread-6] [webserver.handmade.RequestHandler] - request uri : /index.html
*/
```

로깅을 통해 URI가 잘 추출되었음을 확인 할 수 있다.



### 2. 리소스 폴더의 `index.html`반환하기

앞서 추출한 requestUri를 통해 리소스에서 해당 파일을 탐색해야 한다.

```java
Path uriPath = new File("./webapp" + requestUri).toPath();
// ./webapp폴더의 uri파일경로를 반환 받고,

byte[] body = Files.readAllBytes(uriPath);
// readAllBytes를 통해 해당 문서를 byte로 변환한다

responseBody(dos, body);
// byte[]타입인 body를 반환한다.
```

위와 같은 과정을 통해 클라이언트의 URI에 따른 원하는 파일을 반환해 줄 수 있다.



### 로깅 분석

```
15:44:03.826 [INFO ] [main] [webserver.handmade.WebServer] - Web Application Server started 8080 port.
15:44:05.897 [DEBUG] [Thread-0] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60623
15:44:05.900 [DEBUG] [Thread-0] [webserver.handmade.RequestHandler] - request line : GET /index.html HTTP/1.1
15:44:05.902 [DEBUG] [Thread-0] [webserver.handmade.RequestHandler] - request uri : /index.html
15:44:05.910 [DEBUG] [Thread-1] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60624
15:44:05.911 [DEBUG] [Thread-3] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60626
15:44:05.912 [DEBUG] [Thread-5] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60628
15:44:05.912 [DEBUG] [Thread-3] [webserver.handmade.RequestHandler] - request line : GET /js/jquery-2.2.0.min.js HTTP/1.1
15:44:05.914 [DEBUG] [Thread-5] [webserver.handmade.RequestHandler] - request line : GET /js/scripts.js HTTP/1.1
15:44:05.910 [DEBUG] [Thread-2] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60625
15:44:05.914 [DEBUG] [Thread-3] [webserver.handmade.RequestHandler] - request uri : /js/jquery-2.2.0.min.js
15:44:05.917 [DEBUG] [Thread-5] [webserver.handmade.RequestHandler] - request uri : /js/scripts.js
15:44:05.912 [DEBUG] [Thread-1] [webserver.handmade.RequestHandler] - request line : GET /css/bootstrap.min.css HTTP/1.1
15:44:05.912 [DEBUG] [Thread-4] [webserver.handmade.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 60627
15:44:05.921 [DEBUG] [Thread-1] [webserver.handmade.RequestHandler] - request uri : /css/bootstrap.min.css
15:44:05.921 [DEBUG] [Thread-4] [webserver.handmade.RequestHandler] - request line : GET /js/bootstrap.min.js HTTP/1.1
15:44:05.922 [DEBUG] [Thread-4] [webserver.handmade.RequestHandler] - request uri : /js/bootstrap.min.js
15:44:05.921 [DEBUG] [Thread-2] [webserver.handmade.RequestHandler] - request line : GET /css/styles.css HTTP/1.1
15:44:05.922 [DEBUG] [Thread-2] [webserver.handmade.RequestHandler] - request uri : /css/styles.css
```

재밌는 점은, 클라이언트는 분명 `localhost:8080/index.html`이라는 요청을 한 번만 보냈는데, 다수의 `New Client Connect! Connected IP`, `request uri : /js/bootstrap.min.js`, `request uri : /js/scripts.js` 등 여러 요청이 수행됬음을 알 수 있다.

이는 `index.html`에 포함되어있는 요청에 대해서 브라우저가 해석하여 다시 요청하는 과정을 거쳤기 때문이다. 모든 요청에 대해 요청-응답을 반복해야하는 http특성이 드러난다.