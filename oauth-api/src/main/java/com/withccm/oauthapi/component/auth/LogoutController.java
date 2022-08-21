package com.withccm.oauthapi.component.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withccm.oauthapi.common.ApiResponse;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshToken;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshTokenRepository;
import com.withccm.oauthapi.security.token.AuthToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class LogoutController {

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @PostMapping("/logout")
    public ApiResponse postLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthToken token = (AuthToken)authentication.getCredentials();
        User principal = (User) authentication.getPrincipal();

        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserNoAndAccessToken(Long.valueOf(principal.getUsername()), token.getToken());
        userRefreshTokenRepository.delete(userRefreshToken);
        return ApiResponse.success(null);
    }
}
