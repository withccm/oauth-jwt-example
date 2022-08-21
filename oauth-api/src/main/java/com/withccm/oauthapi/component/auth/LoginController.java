package com.withccm.oauthapi.component.auth;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.withccm.oauthapi.common.ApiResponse;
import com.withccm.oauthapi.common.ApiResponseCode;
import com.withccm.oauthapi.component.auth.model.LoginRequestBody;
import com.withccm.oauthapi.component.auth.model.LoginResponse;
import com.withccm.oauthapi.component.auth.service.LoginService;
import com.withccm.oauthapi.component.user.enums.OAuthAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private static final String GOOGLE_TOKEN_INFO_URI = "https://www.googleapis.com/oauth2/v3/tokeninfo";

    private final RestTemplate restTemplate;
    private final LoginService loginService;

    @PostMapping("/login/google")
    public ApiResponse<LoginResponse> postGoogleLogin(@RequestBody LoginRequestBody loginRequestBody) {
        String url = requestUrl(loginRequestBody.getAccessToken());
        try {
            Map<String, Object> googleAccountResult = restTemplate.getForObject(url, Map.class);
            log.debug("url === {}, googleAccountResult === {}", url, googleAccountResult);

            return ApiResponse.success(loginService.login(OAuthAttributes.GOOGLE, googleAccountResult));
        } catch (RestClientException e) {
            log.error("Google Account Api Error url === {}", url, e);
        }

        return ApiResponse.fail(ApiResponseCode.SERVER_ERROR);
    }

    private String requestUrl(String idToken) {
        return UriComponentsBuilder.fromUriString(GOOGLE_TOKEN_INFO_URI)
            .queryParam("id_token", idToken)
            .toUriString();
    }

}
