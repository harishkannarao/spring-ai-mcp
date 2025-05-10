package com.harishkannarao.spring.spring_ai.mcp.server2.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDetailsResolver {

	private final UserDetails defaultUser = User.builder()
		.username("server2-default-user-name")
		.password("")
		.disabled(false)
		.accountExpired(false)
		.credentialsExpired(false)
		.accountLocked(false)
		.authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
		.build();
	private final Map<String, UserDetails> userDetailsMap = Map.ofEntries(
		Map.entry("server2-secret-token", User.builder()
			.username("server2-user-name")
			.password("")
			.disabled(false)
			.accountExpired(false)
			.credentialsExpired(false)
			.accountLocked(false)
			.authorities(List.of(new SimpleGrantedAuthority("ROLE_SERVER_2_USER")))
			.build())
	);

	public Optional<UserDetails> resolve(String token) {
		UserDetails value = userDetailsMap.getOrDefault(token, defaultUser);
		return Optional.ofNullable(value);
	}
}
