package com.withccm.oauthapi.security.token;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenProvider {

    private final Key jwtSecretKey;

    public AuthTokenProvider(String secret) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 리프레시 토큰 생성
     * @param id
     * @param expiry
     * @return
     */
    public AuthToken createAuthToken(String id, LocalDateTime expiry) {
        return new AuthToken(id, toDate(expiry), jwtSecretKey);
    }

    /**
     * 액세스 토큰 생성
     * @param id
     * @param role
     * @param expiry
     * @return
     */
    public AuthToken createAuthToken(String id, String role, LocalDateTime expiry) {
        return new AuthToken(id, role, toDate(expiry), jwtSecretKey);
    }

    private Date toDate(LocalDateTime expiry) {
        return Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 토큰으로 객체 생성
     * @param token
     * @return
     */
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, jwtSecretKey);
    }


}
