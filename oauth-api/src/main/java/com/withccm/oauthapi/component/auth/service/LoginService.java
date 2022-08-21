package com.withccm.oauthapi.component.auth.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.withccm.oauthapi.component.auth.model.LoginResponse;
import com.withccm.oauthapi.component.user.User;
import com.withccm.oauthapi.component.user.UserRepository;
import com.withccm.oauthapi.component.user.UserProfile;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshToken;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshTokenRepository;
import com.withccm.oauthapi.security.AppProperties;
import com.withccm.oauthapi.component.user.enums.OAuthAttributes;
import com.withccm.oauthapi.security.token.AuthToken;
import com.withccm.oauthapi.security.token.AuthTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

	private final UserRepository userRepository;
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final UserRefreshTokenRepository userRefreshTokenRepository;

	public LoginResponse login(OAuthAttributes oauthType, Map<String, Object> attributes) {
		UserProfile userProfile = OAuthAttributes.extract(oauthType.getRegistrationId(), attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
		User user = getAndSave(userProfile); // 신규인경우 DB에 저장

		Long userNo = user.getUserNo();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime accessTokenExpires = now.plusMinutes(appProperties.getAuth().getTokenExpiry());
		AuthToken accessToken = tokenProvider.createAuthToken(
			userNo.toString(),
			user.getRoleKey(),
			accessTokenExpires
		);

		// refresh 토큰 설정
		LocalDateTime refreshTokenExpires = now.plusDays(appProperties.getAuth().getRefreshTokenExpiry());
		AuthToken refreshToken = tokenProvider.createAuthToken(
			appProperties.getAuth().getTokenSecret(),
			refreshTokenExpires
		);

		// DB 저장
		UserRefreshToken userRefreshToken = UserRefreshToken.builder()
			.userNo(userNo)
			.refreshToken(refreshToken.getToken())
			.refreshTokenExpires(refreshTokenExpires)
			.accessToken(accessToken.getToken())
			.accessTokenExpires(accessTokenExpires)
			.createdDate(now)
			.updatedDate(now)
			.build();
		userRefreshTokenRepository.saveAndFlush(userRefreshToken);

		return LoginResponse.builder()
			.accessToken(accessToken.getToken())
			.accessTokenExpires(accessTokenExpires)
			.refreshToken(userRefreshToken.getRefreshToken())
			.refreshTokenExpires(refreshTokenExpires)
			.build();
	}

	private User getAndSave(UserProfile userProfile) {
		// 회원가입절차가 따로 없습니다. 분리하고 싶은경우 사용자가 없는경우 예외 처리를 하면된다.
		Optional<User> userOptional = userRepository.findByOauthTypeAndOauthId(userProfile.getOauthType(), userProfile.getOauthId());
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		User user = userProfile.toUser();
		log.info("user === {}", user);
		return userRepository.save(user);
	}
}

