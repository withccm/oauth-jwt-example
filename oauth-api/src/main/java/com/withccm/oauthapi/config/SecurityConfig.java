package com.withccm.oauthapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.cors.CorsUtils;

import com.withccm.oauthapi.component.user.enums.Role;
import com.withccm.oauthapi.security.exception.RestAuthenticationEntryPoint;
import com.withccm.oauthapi.security.filter.TokenAuthenticationFilter;
import com.withccm.oauthapi.security.handler.TokenAccessDeniedHandler;
import com.withccm.oauthapi.security.token.AuthTokenProvider;
import com.withccm.oauthapi.security.token.AuthenticationFactory;

import lombok.RequiredArgsConstructor;

@EnableJdbcHttpSession(maxInactiveIntervalInSeconds=3600*6)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

	private final AuthTokenProvider tokenProvider;
	private final AuthenticationFactory authenticationFactory;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	// STATELESS 옵션은 security가 세션을 생성하지 않음
			.and()
			.csrf().disable()	// stateless rest api이기 때문에 csrf을 사용하지 않음
			.formLogin().disable()	// login 페이지 비활성화 (웹기반페이지)
			.httpBasic().disable()	// Http basic Auth 기반 인증 비활성화, https://user-images.githubusercontent.com/17292878/185782346-28737134-e5a3-4fe6-83b4-5fb58738305d.png
			.exceptionHandling()	// 예외 처리 정의
			.authenticationEntryPoint(restAuthenticationEntryPoint)	// 인증 실패시
			.accessDeniedHandler(tokenAccessDeniedHandler)  //  인가(역할/권한없음) 실패시
			.and()
			.authorizeRequests() // 리소스의 접근 역할/권한 설정
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()	// PreFlight 요청 허용 (브라우저에서 cross-origin 요청전 options 매소드로 PreFlight를 전송)
			.antMatchers(
				"/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html" // swagger 설정
				, "/api/v*/auth/**"	// 인증관련
			).permitAll()
			.antMatchers("/api/**")
				.hasAnyAuthority(Role.USER.getKey(), Role.ADMIN.getKey())
			.antMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.getKey())
			.anyRequest().authenticated()	//그외 나머지 리소스들은 무조건 인증을 완료해야 접근이 가능하다는 의미
			;

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);//스프링시큐리티의 기본 인증 처리 담당 필터인 UsernamePasswordAuthenticationFilter 앞에 커스텀 필터를 추가합니다.
	}

	/*
	 * 토큰 필터 설정
	 * */
	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider, authenticationFactory);
	}
}
