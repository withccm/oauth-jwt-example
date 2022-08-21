package com.withccm.oauthapi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiResponseCode {
	SUCCESS(200, "SUCCESS"),

	INVALID_ACCESS_TOKEN(30001, "Invalid access token."),
	INVALID_REFRESH_TOKEN(30002, "Invalid refresh token."),
	NOT_EXPIRED_TOKEN_YET(30003, "Not expired token yet."),

	SERVER_ERROR(40001, "Server Error"),
	;

	private final int code;
	private final String message;

}
