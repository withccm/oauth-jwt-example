package com.withccm.oauthapi.security.token;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.security.Keys;

import static org.assertj.core.api.Assertions.assertThat;

import com.withccm.oauthapi.security.constants.SecurityConstants;

public class AuthTokenTest {

	private Logger log = LoggerFactory.getLogger(AuthTokenTest.class);

	private static Key jwtSecretKey;

	@BeforeAll
	public static void before() {
		jwtSecretKey = Keys.hmacShaKeyFor("0000000000000000000000000000000000001".getBytes());
	}

	@Test
	public void 정상토큰_테스트() {
		// given
		Date now = new Date();
		long expiry = 5000L;
		AuthToken token = new AuthToken("userNo", new Date(now.getTime() + expiry), jwtSecretKey);

		// when
		boolean result = token.validate();

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void 정상토큰_아이디_테스트() {
		// given
		Date now = new Date();
		long expiry = 5000L;
		String id = "userNo";
		AuthToken token = new AuthToken(id, new Date(now.getTime() + expiry), jwtSecretKey);

		// when
		String result = token.getTokenClaims().getSubject();

		// then
		assertThat(result).isEqualTo(id);
	}

	@Test
	public void 정상토큰_claim변수테스트() {
		// given
		Date now = new Date();
		long expiry = 5000L;
		String id = "userNo";
		String role = "user";
		AuthToken token = new AuthToken(id, role, new Date(now.getTime() + expiry), jwtSecretKey);

		// when
		String result = token.getTokenClaims().get(SecurityConstants.AUTHORITIES_KEY, String.class);

		// then
		assertThat(result).isEqualTo(role);
	}

	@Test
	public void 시간만료() throws InterruptedException {
		// given
		Date now = new Date();
		long expiry = 5000L;
		AuthToken token = new AuthToken("userNo", new Date(now.getTime() + expiry), jwtSecretKey);

		// when
		Thread.sleep(expiry + 100L);
		boolean result = token.validate();

		// then
		assertThat(result).isFalse();
	}
}
