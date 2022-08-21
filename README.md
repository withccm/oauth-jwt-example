# oauth-sample
API 서버와 WEB 클라이언트 oauth 로그인 인증 예제로 구성

------------
## 프로젝트 구성

### API
* Oauth 토근으로 인증할 수 있는 프로젝트 구성
* JWT 사용하여 Access Token, Refresh Token 인증 로직 구현
  * JWT(Json Web Token)는 사용자 인증을 위한 암호화된 토큰이다.
  * Session서버를 구성이 필요없어, 확장성이 있다.
  * JWT를 이용하여 Access Token, Refresh Token 각각 토큰을 생성한다.
  * Access Token은 만료 시간이 짧은 토큰, 반면 Refresh Token은 만료 시간이 길다.
  * Access Token이 탈취될 경우 보안 취약점이 발생할 수 있다. 만료시간을 짧게함으로 피해를 줄일 수 있다.(추가로 구현하면 서버에서 기존 토큰을 강제 로그아웃 시키는 기능도 가능하다.)    
    Access Token이 만료되는 경우 매번 로그인을 해야하는 번거로움이 생길 수 있는데, 이를 보완하는 것이 Refresh Token이다.
    Refresh Token은 Access Token이 만료되 시점에 신규 토큰으로 갱신하게 해준다.  
     
* 다양한 Client(AOS, IOS등) 상관없이 확장 가능
* 구글 샘플
* 역할 샘플

### Web Client
* 구글 로그인 SDK 구현
* Access Token 만료시 Refresh Token 호출 로직
* 샘플 API 호출

------------
## 토큰 인증 과정 UML
<img src="https://user-images.githubusercontent.com/17292878/184908486-839ed384-6aa7-43f0-8632-366552013822.png" width="700" alt="토큰 인증 과정 UML 이미지" />

### access 토큰과 refresh 토큰
access 토큰은 API 요청시에 헤더에 포함하여 인증값으로 사용한다. access 토큰이 탈퇴당하는 경우 보안에 취약해진다. 이를 방지하기 위해 access 토큰의 유효시간은 짧게 제공한다.  
이때 문제점이 발생한다. access 토큰이 만료 될때마다, 사용자에게 로그인을 요구한다면 사용자는 서비스 이용에 번거로움이 생긴다. 이때 사용하는 것이 refresh 토큰이다. (refresh 토큰의 만료시간은 길게 설정하는 편이다.)  
서버에서 access 토큰이 만료되었다는 응답을 받았을때, refresh 토큰을 사용하여 access 토큰을 새로 갱신할 수 있다.

------------
#### 자세한 내용은 하위 프로젝트 README 참고