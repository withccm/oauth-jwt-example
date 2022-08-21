package com.withccm.oauthapi.component.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withccm.oauthapi.common.ApiResponse;
import com.withccm.oauthapi.component.auth.model.LoginResponse;
import com.withccm.oauthapi.component.auth.model.RefreshRequestBody;
import com.withccm.oauthapi.component.auth.service.AuthRefreshService;
import com.withccm.oauthapi.utils.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthRefreshService authRefreshService;

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> postRefreshToken(HttpServletRequest request, @RequestBody RefreshRequestBody reqBody) {
        String accessToken = HeaderUtil.getAccessToken(request);
        return ApiResponse.success(authRefreshService.refresh(reqBody.getRefreshToken(), accessToken));
    }
}
