package com.withccm.oauthapi.component.auth.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

	private String accessToken;
	private LocalDateTime accessTokenExpires;
	private String refreshToken;
	private LocalDateTime refreshTokenExpires;
}
