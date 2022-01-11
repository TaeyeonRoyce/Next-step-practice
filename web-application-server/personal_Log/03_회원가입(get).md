# 회원 가입

첫번째 요구 사항을 완수하여 `index.html`을 화면에 보이도록 하였다. 이 뷰에 이어서 요구 사항 2를 진행하고자 한다.

### "회원가입" 메뉴를 클릭하면 http://localhost:8080/user/form.html로 이동하고 회원가입 내용을 입력하면 model.User클래스에 저장하도록하여라

### 해결 아이디어.

1. user/form.html로 이동
2. 회원 가입 폼 요청에 대한 처리
3. model.User로 저장하기

### 1. user/form.html로 이동

```html
<li><a href="user/form.html" role="button">회원가입</a></li>
```



### 2. 회원 가입 폼 요청에 대한 처리

`index.html`을 보면, 회원 가입 form으로 이동할 수 있고, 이 form에는 다음과 같은 정보를 입력할 수 있다.

- 사용자 아이디
- 비밀번호
- 이름
- 이메일

이렇게 입력된 정보는
`<form name="question" method="get" action="/user/create">` 이라는 `form`속성을 통해 전달된다.

`GET` 메서드로 `/user/create`라는 `action`이 수행됨을 알 수 있다.

이 경우 다음과 같은 쿼리문이 작성됨을 파악해야 한다.

```
/user/create?userId={userId}&password={password}&name={name}&email={email}
```



- ##### 쿼리문으로 부터 데이터 추출

HttpRequest에 대하여 다음과 같은 요청을 얻을 수 있다.

```
GET /user/create?userId={userId}&password={password}&name={name}&email={email} HTTP/1.1
```

기존에 사용하였던 `RequestHandler`를 통해 `URI`를 추출하고, 이어서 데이터들을 추출하도록 하였다.

우선 URI로부터 쿼리문을 추출하는 메서드를 작성하였다.

```java
private String extractQueryFromURI(String URI) {
  String queryStartChar = "\\?";
  String[] split = URI.split(queryStartChar);
  return split[1];
}
```

쿼리문은 `?`로 시작하기 때문에 이를 통해 분리하도록 하였다.

그 후 제공된 `HttpRequestUtils`의 `parseQueryString()`을 활용하여 쿼리문을 파라미터 처럼 사용할 수 있는 메서드를 통해 여러 인자들을 분리 하고, `get()`을 통해 User데이터를 추출 및 저장하였다.

```java
if (requestURI.startsWith("/user/create")) {
  String queryString = extractQueryFromURI(requestURI);
  log.debug("queryString: {}", queryString);

  Map<String, String> params =
    HttpRequestUtils.parseQueryString(queryString);
  User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                       params.get("email"));
  log.debug("User : {}", user);
}
```

