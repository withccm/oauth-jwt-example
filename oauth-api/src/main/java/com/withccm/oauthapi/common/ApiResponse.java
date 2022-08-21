package com.withccm.oauthapi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return success(ApiResponseCode.SUCCESS, data);
    }

    public static <T> ApiResponse<T> success(ApiResponseCode code, T data) {
        return new ApiResponse(code.getCode(), code.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(ApiResponseCode code) {
        return new ApiResponse(code.getCode(), code.getMessage(), null);
    }
}
