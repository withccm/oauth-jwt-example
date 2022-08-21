package com.withccm.oauthapi.security.token;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.withccm.oauthapi.common.ApiResponseCode;
import com.withccm.oauthapi.common.exception.CustomException;
import com.withccm.oauthapi.component.user.refreshtoken.UserRefreshTokenRepository;
import com.withccm.oauthapi.security.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationFactory {

	private final UserRefreshTokenRepository userRefreshTokenRepository;

	public Authentication getAuthentication(AuthToken authToken) {
		if(authToken.validate()) {
			Claims claims = authToken.getTokenClaims();

			// 토큰이 유효한 토큰인지 디비 테이블에서 검사
			// UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserNoAndAccessToken(Long.valueOf(claims.getSubject()), authToken.getToken());
			// if (Objects.isNull(userRefreshToken)) {
			// 	throw new TokenValidFailedException();
			// }

			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[]{claims.get(SecurityConstants.AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			log.debug("claims subject := [{}]", claims.getSubject());
			User principal = new User(claims.getSubject(), "", authorities);
			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		} else {
			throw new CustomException(ApiResponseCode.INVALID_ACCESS_TOKEN);
		}
	}
}
