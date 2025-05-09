package com.harishkannarao.spring.spring_ai.mcp.server1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		http
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.headers(headers ->
				headers.httpStrictTransportSecurity(hstsConfig -> hstsConfig.includeSubDomains(true)))
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(this::configureUrlAuthorization)
			.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
				httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(
					(request, response, accessDeniedException) ->
						response.setStatus(HttpStatus.FORBIDDEN.value()));
				httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
					new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
			});

		return http.build();
	}

	private void configureUrlAuthorization(
		AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
		// this /error mapping is required to return 400 or 500 or other statuses.
		// otherwise 401 is returned for all errors
		auth.requestMatchers("/error").permitAll();

		auth.requestMatchers(
			"/sse",
			"/mcp/message",
			"/tool-definitions"
		).permitAll();

		auth.anyRequest().denyAll();
	}
}
