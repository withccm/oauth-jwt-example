package com.withccm.oauthapi.utils;

import javax.servlet.http.HttpServletRequest;

import com.withccm.oauthapi.security.constants.SecurityConstants;

public class HeaderUtil {

    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
