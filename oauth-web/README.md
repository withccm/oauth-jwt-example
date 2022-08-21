# Oauth 로그인 샘플 React App
로그인과 Refresh Token으로 토큰 갱신 과정을 제공한다.

------------

## 실행 방법

### `npm start` or `yarn start`
[http://localhost:3000](http://localhost:3000)에서 확인 가능합니다.

------------
## 환경설정
### .env 파일
1. REACT_APP_API_URL : API 서버 도메인 정의

------------
## Oauth 로그인 설정
각각 프로바이더에 맞게 설정하면된다.

### 구글
사용자 인증 정보를 사용하기 위해 등록을 해야한다.

1. [구글 클라우드 접속](https://console.cloud.google.com/apis/dashboard)
2. 프로젝트 만들기  
   <img src="https://user-images.githubusercontent.com/17292878/183283729-f9fc77df-9e87-4dcc-bc85-a5002c2b9588.png" width="600" alt="프로젝트 만들기 이미지" />
3. 왼쪽 사용자 인증 정보 선택
4. 오른쪽 CREATE CREDENTIALS 선택  
   <img src="https://user-images.githubusercontent.com/17292878/183283892-1839ccda-53af-49bb-aa8c-79ea744983c9.png" width="600" alt="사용자 인증 정보 이미지" />
5. OAuth 클라이언트 ID 선택   
   <img src="https://user-images.githubusercontent.com/17292878/183284087-6e423f2e-ded6-4228-8a3b-4087d081207c.png" width="400" alt="OAuth 클라이언트 ID 선택 이미지" />
6. OAuth 동의화면 생성   
   (OAuth 동의화면이 이미 만들어졌다면 이 부분은 스킵해도 됩니다.)
    - 6-1. 동의 화면 구성 선택  
      <img src="https://user-images.githubusercontent.com/17292878/183284241-e06bac5f-b457-4c06-b204-1827fee63fff.png" width="400" alt="동의화면 만들기 이미지" />
    - 6-2. 외부 선택하고 만들기  
      <img src="https://user-images.githubusercontent.com/17292878/183284352-94534058-a643-495a-97cc-3fb7c17cd799.png" width="400" alt="외부 선택 이미지" />
    - 6-3. 앱정보 입력 (필수값만 입력하면 됩니다.)  
      <img src="https://user-images.githubusercontent.com/17292878/183284589-b0d3ebc6-c98b-4880-a76e-b0c3cfbe2157.png" width="400" alt="앱정보 입력 이미지" />
7. 웹 애플리케이션 선택    
   구현하고자 하는 유형을 선택해주시면 됩니다. 예시는 웹으로 되어 있기 때문에 웹 애플리케이션을 선택했습니다.  
   <img src="https://user-images.githubusercontent.com/17292878/183284727-467f39e3-21a7-4a96-a165-275024a0c451.png" width="400" alt="웹 애플리케이션 선택 이미지" />
8. 웹 > 요청할 URL 추가  
   http://localhost:3000 리액트 프로젝트 추가   
   <img src="https://user-images.githubusercontent.com/17292878/183285239-f6afc1a7-02d3-4998-8190-24b47af6d21a.png" width="400" alt="요청할 URL 추가 이미지" />
9. 우측에 있는 클라이언트 ID를 사용하면됩니다.   
   <img src="https://user-images.githubusercontent.com/17292878/183290996-4ffe6527-e60d-4142-990a-4c9b3fa95f9b.png" width="700" alt="클라이언트 ID 이미지" />

#### 리액트
1. 라이브러리 설치
```
npm install react-google-login
```
2. clientId 정의
```
const clientId = "앞의 이미지 9번 클라이언트 ID";
```
3. 로그인 버튼 정의
```
<GoogleLogin
    clientId={clientId}
    responseType={"id_token"}
    onSuccess={onSuccessGoogle}
    onFailure={onFailureGoogle}/>
```
4. 성공/실패 함수 구현 
```
    const onSuccessGoogle = async(response) => {
        alert('로그인에 성공했습니다.')
    }

    const onFailureGoogle = (error) => {
        console.log(error);
        alert('로그인에 실패했습니다.')
    }
```

------------
## 주요 코드
### 로그인 페이지 Login.jsx

#### 로그인 순서
1. 프로바이더(예로 구글) OAuth 로그인  
   (OAuth 토큰을 받은 상태)

2. OAuth 로그인 성공시 해당 토큰으로 API서버에 로그인시도
```
    const onSuccessGoogle = async(response) => {
        const loginRes = await login(response.tokenId, 'google')
        if (loginRes.error) {
            alert('로그인에 실패했습니다.')
        } else {
            window.location.replace('//' + window.location.host + params.redirect_uri)
        }
    }
```
* 구글 로그인 성공시 response.tokenId에서 토큰을 획득한다.  
* 해당 토큰으로 API에 로그인 요청한다.

3. 성공시 Access Token과 Refresh Token 저장
```
export const login = async (tokenId, type) => {
    return await axios.post('/api/v1/auth/login/' + type, {
        accessToken : tokenId
    }, {
    }).then(response => {
        AuthUtils.setToken(response.data.data)
        return response
    }).catch(error => {
        console.log(error)
        return error.response.data
    })
}
```
* API에 로그인 성공시 AuthUtils.setToken 함수로 인증값(Access Token과 Refresh Token)을 저장한다.

4. redirect_uri로 이동, 없는경우 /로 이동

### 인증 유틸리티 AuthUtils.js
토큰 관리를 도와준다.

### API 유틸리티 ApiUtils.js
API 요청 편하게 할 수 있도록 기본 설정이 되어 있다.
1. 인증(Authorization) 전달
2. 인증필요 API 요청시, 로그인 페이지로 이동
3. 인증 만료시 토큰 리프레시후 재요청

예시>
```
apiIClient.get('/api/v1/myProfile').then(response => {
    console.log(response)
}).catch(error => {
    console.log('error', error)
})
```

#### axios 인터셉터
```
// 요청 인터셉터 추가
axios.interceptors.request.use(
  (config) => {
    // 요청을 보내기 전에 호출
    return config;
  },
  (error) => {
    // 오류 요청을 보내기전 호출
    return Promise.reject(error);
  });

// 응답 인터셉터 추가
axios.interceptors.response.use(
  (response) => {
    // 응답 보내기 전에 호출
    return response;
  },
  (error) => {
    // 오류 응답 보내기 전에 호출
    return Promise.reject(error);
  });
```
공통으로 처리될 부분은 interceptors를 정의한다.
ex> 인증, 오류 처리
```
    client.interceptors.request.use((config) => {
        const token = AuthUtils.getToken()
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    });
    
    client.interceptors.response.use(
        (response) => {
            return response.data;
        },
        async (error) => {
            if (!error.response) {  // timeout
                return Promise.reject(error);
            }

            if (error.response.status === 401) {
                if (AuthUtils.getToken() && AuthUtils.getRefreshToken()) {
                    const refreshTokenRes = await refreshToken()
                    if (refreshTokenRes.error) {
                        // console.log('토큰 리프레시 실패')
                    } else {
                        AuthUtils.setToken(refreshTokenRes.data.data)
                        return client.request(error.config)
                    }
                }

                alert('로그인이 필요합니다.')
                goLogin()
            }

            return Promise.reject(error);
        }
    );    
```
