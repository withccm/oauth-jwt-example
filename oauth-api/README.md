# JWT Oauth 로그인 샘플 API
JWT 로그인 인증을 제공한다.

------------
## 개발 환경
 * Java 11
 * Gradle 7.1
 * Mysql -> MariaDB (변경예정)

### 주요 자바 라이브러리
 * Spring Boot 2.5.2
 * Spring security
 * JPA
 * jjwt-api
 * Lombok

### 데이터베이스 접속정보 
- host : localhost
- port : 3306
- database : mytest
- username : github
- password : test1234  

수정해서 사용하시면 됩니다.

------------
## 서버 접속 정보
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
### API 목록
1. POST /api/v1/auth/login/google   
[구글 로그인](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/login-controller/postGoogleLogin) 
2. POST /api/v1/logout  
[로그아웃](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/logout-controller/postLogout)
3. POST /api/v1/auth/refresh  
[토근 리프레시](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/auth-controller/postRefreshToken)
4. GET /api/v1/myProfile  
[나의 프로필](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/user-controller/getMyProfile)

------------
## 테이블 스키마
```
CREATE TABLE `user` (
  `userNo` bigint(20) NOT NULL AUTO_INCREMENT,
  `oauthType` varchar(50) DEFAULT NULL COMMENT 'ProviderType\n구글, 페이스북 등등',
  `oauthId` varchar(2555) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userNo`)
);

CREATE TABLE `userRefreshToken` (
  `refreshTokenSeq` bigint(20) NOT NULL AUTO_INCREMENT,
  `userNo` bigint(20) NOT NULL,
  `refreshToken` varchar(300) DEFAULT NULL,
  `refreshTokenExpires` datetime DEFAULT NULL,
  `accessToken` varchar(300) DEFAULT NULL,
  `accessTokenExpires` datetime DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `updatedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`refreshTokenSeq`),
  KEY `idx1` (`userNo`,`accessToken`),
  KEY `idx2` (`userNo`,`refreshToken`)
);

CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookname` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
```


------------
## 사용하기 전에 확인할 부분
기본적으로 사용가능하지만 
1. application-oauth.yml 
   - jwt.secret
      - jwt secret key 설정 
   - app.auth 하위설정
      - 토큰 관련 설정  
      - 토큰 시간 지정
2. application-datasource.yml
   - 데이터베이스 접속설정 
   - spring.datasource 하위설정 
   - url, username, password, hikari.maximum-pool-size

------------
## 주요 코드

### AuthToken 클래스
JWT 라이브러리를 사용해 토큰을 관리하는 AuthToken 클래스를 알아보자.    
AuthToken 클래스는 JWT 라이브러리에 생성된 토큰을 쉽게 사용하도록 도와준다.  
JWT API로 토큰을 생성할때 인증에 필요한 정보를 함께 암호시킨다.  
전송되는데이터(Payload)에 앞에서 언급한 값이 세팅하게 되는데, 이때 key-value형식의 데이터 조각을 Claim라 부른다. 샘플에서는 사용자식별자(userNo)와 역할이 포함되어 있다.
```
{
  "sub" : "321",
  "role" : "ROLE_USER"
}
```
AuthTokenTest.java 파일에서 다양한 예제가 구현되어 있다.

### API 공통 응답
API는 다음과 같은 형식으로 응답이 이루어진다. 
```
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;
    ...
}
```
### 오류 코드 정의
```
public enum ApiResponseCode {
	...
	INVALID_ACCESS_TOKEN(30001, "Invalid access token."),
	INVALID_REFRESH_TOKEN(30002, "Invalid refresh token."),
	NOT_EXPIRED_TOKEN_YET(30003, "Not expired token yet."),
	...
}
```
### API 오류 처리
GlobalAPIExceptionAdvice 클래스가 예외처리를 담당한다. 

------------
## Spring Security 설정

1. RestAuthenticationEntryPoint.java
   * 인증 실패(토큰 만료) 응답을 API 에러로 변경
2. TokenAccessDeniedHandler.java
   * 인가 실패(역할/권한없음) 응답 처리
3. AuthTokenProvider.java
   * 토큰 생성 역할  
4. AuthenticationFactory.java
   * 토큰에서 사용자 정보 추출
6. TokenAuthenticationFilter.java
   * 토큰 유효성 확인

------------
## CORS 설정
_교차 출처 리소스 공유(Cross-Origin Resource Sharing, CORS)는 추가 HTTP 헤더를 사용하여, 한 출처에서 실행 중인 웹 애플리케이션이 다른 출처의 선택한 자원에 접근할 수 있는 권한을 부여하도록 브라우저에 알려주는 체제이다. - MDN_

브라우저에서는 기본적으로 같은 출처(Origin)의 자원만 요청을 보안의 이유로 허용하고 있다.
출처은 scheme, host, port 으로 구성되어 있다. 같은 출처는 3가지 요소가 같은 경우를 말한다.
```
http:// -> scheme
localhost -> host
:8080 -> port
```
  
예제는 편의상 모든 출처 허용으로 설정되어 있고 본인의 서비스에 맞게 설정하길 바란다.
```
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowCredentials(true);
	}
}
```