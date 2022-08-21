package com.withccm.oauthapi.common.exception;

import com.withccm.oauthapi.common.ApiResponseCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

	private ApiResponseCode code;
}
