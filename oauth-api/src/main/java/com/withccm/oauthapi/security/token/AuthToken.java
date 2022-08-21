package com.withccm.oauthapi.security.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Objects;

import com.withccm.oauthapi.security.constants.SecurityConstants;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

	@Getter
	private final String token;
	private final Key jwtSecretKey;

	AuthToken(String id, Date expiry, Key jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
		this.token = createAuthToken(id, expiry);
	}

	AuthToken(String id, String role, Date expiry, Key jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
		this.token = createAuthToken(id, role, expiry);
	}

	private String createAuthToken(String id, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.signWith(jwtSecretKey, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	private String createAuthToken(String id, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.claim(SecurityConstants.AUTHORITIES_KEY, role)
			.signWith(jwtSecretKey, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(jwtSecretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SecurityException e) {
			log.info("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
		}
		return null;
	}

	public Claims getExpiredTokenClaims() {
		try {
			Jwts.parserBuilder()
				.setSigningKey(jwtSecretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			return e.getClaims();
		}
		return null;
	}

	public boolean isExpired() {
		return Objects.nonNull(getExpiredTokenClaims());
	}
}
