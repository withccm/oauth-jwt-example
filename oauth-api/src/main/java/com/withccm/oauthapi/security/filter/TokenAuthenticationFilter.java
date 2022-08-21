package com.withccm.oauthapi.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.withccm.oauthapi.security.constants.SecurityConstants;
import com.withccm.oauthapi.security.token.AuthToken;
import com.withccm.oauthapi.security.token.AuthTokenProvider;
import com.withccm.oauthapi.security.token.AuthenticationFactory;
import com.withccm.oauthapi.utils.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final AuthTokenProvider tokenProvider;
	private final AuthenticationFactory authenticationFactory;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain)  throws ServletException, IOException {

		String token = HeaderUtil.getAccessToken(request);
		log.debug("token ==={}", token);
		AuthToken accessToken = tokenProvider.convertAuthToken(token);

		if (accessToken.validate()) {
			Authentication authentication = authenticationFactory.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else if (StringUtils.isNotBlank(token) && accessToken.isExpired()) { // access token이 만료된경우, refresh 요청
			response.setHeader(SecurityConstants.NEED_REFRESH, SecurityConstants.NEED_REFRESH_TRUE);
		}

		filterChain.doFilter(request, response);
	}

}
