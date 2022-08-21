package com.withccm.oauthapi.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver handlerExceptionResolver;

	/**
	 * 인가 실패
	 * - 접근 역할/권한이 없을때
	 *
	 * @param request
	 * @param response
	 * @param accessDeniedException
	 * @throws IOException
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		log.debug("tokenAccessDeniedHandler requestURI {}", request.getRequestURI());
		response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
		handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
	}
}
