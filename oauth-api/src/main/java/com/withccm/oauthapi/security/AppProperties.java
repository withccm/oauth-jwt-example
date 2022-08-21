package com.withccm.oauthapi.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Auth auth = new Auth();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        private String tokenSecret;         // refresh token 생성할때 확인값
        private long tokenExpiry;           // 30분
        private long refreshTokenExpiry;    // 60일
        private long tempTokenExpiry;       // 30초
        private long refreshDay;            // 7일
    }
}
