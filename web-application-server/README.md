# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 0 - 원격서버 설정 및 소스코드 분석

- [원격서버 설정하기](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/01_원격%20서버.md)

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

* [클라이언트 요청 분석 및 응답하기](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/02_index.html응답.md)

### 요구사항 2 - get 방식으로 회원가입
* [회원가입 요청에 대한 처리 및 분석 후 회원가입 정보를 user로 저장하기](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/03_회원가입(get).md)
* [회원가입 데이터에 대한 예외처리하기](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/04_회원데이터예외처리.md)

### 요구사항 3 - post 방식으로 회원가입
* [회원가입 기능 POST메서드로...](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/05_회원가입(post).md)

### 요구사항 4 - redirect 방식으로 이동
* [상태코드를 활용한 redirect](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/06_리다이렉트.md)

### 요구사항 5 - cookie
* [쿠키를 활용한 로그인 구현](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/07_로그인.md)
* [로그인 여부에 따른 사용자 목록 출력하기](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/08_사용자목록%20출력.md)

# 리팩토링

### 요청 데이터를 처리하는 로직을 별도 클래스로 분리하기

- [HttpRequest 테스트 코드 작성](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/src/test/java/webserver/HttpRequestTest.java)

- [HttpResponse 테스트 코드 작성](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/src/test/java/webserver/HttpResponseTest.java)

- [TDD를 적용하여 요청 데이터 처리 로직(HttpRequest) 구현](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/src/main/java/webserver/HttpRequest.java)

  [HttpRequest에 대한 리팩토링](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/personal_Log/09_HttpRequest%20리팩토링.md)

- [TDD를 적용하여 응답 처리 로직(HttpResponse) 구현](https://github.com/TaeyeonRoyce/Next-step-practice/blob/master/web-application-server/src/main/java/webserver/HttpResponse.java)

- 