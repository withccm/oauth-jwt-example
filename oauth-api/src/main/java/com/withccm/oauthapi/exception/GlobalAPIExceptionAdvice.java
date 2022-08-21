package com.withccm.oauthapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.withccm.oauthapi.common.ApiResponse;
import com.withccm.oauthapi.common.ApiResponseCode;
import com.withccm.oauthapi.common.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalAPIExceptionAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected ApiResponse exception(Exception e) {
		return makeErrorResult(ApiResponseCode.SERVER_ERROR, e);
	}

	@ExceptionHandler(CustomException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse customException(CustomException e) {
		return makeErrorResult(e.getCode(), e);
	}


	private ApiResponse makeErrorResult(ApiResponseCode code, Throwable e) {
		return ApiResponse.fail(code);
	}
}
