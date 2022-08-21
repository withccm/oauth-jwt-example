package com.withccm.oauthapi.component.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.withccm.oauthapi.common.ApiResponseCode;
import com.withccm.oauthapi.common.exception.CustomException;
import com.withccm.oauthapi.component.auth.model.LoginResponse;
import com.withccm.oauthapi.component.user.enums.Role;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshToken;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshTokenRepository;
import com.withccm.oauthapi.security.AppProperties;
import com.withccm.oauthapi.security.constants.SecurityConstants;
import com.withccm.oauthapi.security.token.AuthToken;
import com.withccm.oauthapi.security.token.AuthTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthRefreshService {

	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final UserRefreshTokenRepository userRefreshTokenRepository;

	public LoginResponse refresh(String refreshToken, String accessToken) {
		if (StringUtils.isBlank(refreshToken)) {
			throw new CustomException(ApiResponseCode.INVALID_REFRESH_TOKEN);
		}

		// access token 확인
		AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
		if (authToken.validate()) {
			throw new CustomException(ApiResponseCode.INVALID_ACCESS_TOKEN);
		}

		// expired access token 인지 확인
		if (authToken.isExpired() == false) {
			throw new CustomException(ApiResponseCode.NOT_EXPIRED_TOKEN_YET);
		}
		Claims claims = authToken.getExpiredTokenClaims();
		String userNo = claims.getSubject();
		Role roleType = Role.of(claims.get(SecurityConstants.AUTHORITIES_KEY, String.class));

		// refresh token 확인
		AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
		if (!authRefreshToken.validate()) {
			throw new CustomException(ApiResponseCode.INVALID_REFRESH_TOKEN);
		}
		// 임의로 조작한 토큰 인지 확인
		if (!appProperties.getAuth().getTokenSecret().equals(authRefreshToken.getTokenClaims().getSubject())) {
			throw new CustomException(ApiResponseCode.INVALID_REFRESH_TOKEN);
		}

		// userNo refresh token 으로 DB 확인
		UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserNoAndRefreshToken(Long.valueOf(userNo), refreshToken);
		if (userRefreshToken == null) {
			throw new CustomException(ApiResponseCode.INVALID_REFRESH_TOKEN);
		}
		if (!StringUtils.equals(userRefreshToken.getAccessToken(), accessToken)) {    // accessToken과 refreshToken 쌍이 맞는지 확인
			throw new CustomException(ApiResponseCode.INVALID_REFRESH_TOKEN);
		}

		// 신규 토큰 생성
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime accessTokenExpires = now.plusMinutes(appProperties.getAuth().getTokenExpiry());
		AuthToken newAccessToken = tokenProvider.createAuthToken(
			userNo,
			roleType.getKey(),
			accessTokenExpires
		);

		// refresh 토큰 기간이 N일 이하로 남은 경우, refresh 토큰 갱신
		LocalDateTime oldRefreshTokenExpires = authRefreshToken.getTokenClaims().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		long gap = Duration.between(oldRefreshTokenExpires, now).toDays();
		if (gap < appProperties.getAuth().getRefreshDay()) {
			// refresh 토큰 설정
			long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
			LocalDateTime refreshTokenExpires = now.plusMinutes(appProperties.getAuth().getRefreshTokenExpiry());
			authRefreshToken = tokenProvider.createAuthToken(
				appProperties.getAuth().getTokenSecret(),
				refreshTokenExpires
			);

			// DB에 refresh 토큰 업데이트
			userRefreshToken.setRefreshToken(authRefreshToken.getToken());
			userRefreshToken.setRefreshTokenExpires(refreshTokenExpires);
		}
		userRefreshToken.setAccessToken(newAccessToken.getToken());
		userRefreshToken.setAccessTokenExpires(accessTokenExpires);
		userRefreshToken.setUpdatedDate(now);
		userRefreshTokenRepository.saveAndFlush(userRefreshToken);

		return LoginResponse.builder()
			.accessToken(newAccessToken.getToken())
			.accessTokenExpires(accessTokenExpires)
			.refreshToken(userRefreshToken.getRefreshToken())
			.refreshTokenExpires(userRefreshToken.getRefreshTokenExpires())
			.build();
	}
}
