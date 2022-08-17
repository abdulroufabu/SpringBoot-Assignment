package com.test.statement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.test.statement.utils.AuthorizationUtil.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationEntryPoint entryPoint;

	@Autowired
	private final PasswordEncoder passwordEncoder;

	public SecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
		.antMatchers("/api/v1/account")
			.hasAnyRole(Role.ADMIN.name(), Role.USER.name())
		.anyRequest().authenticated()
		.and().httpBasic()
		.authenticationEntryPoint(entryPoint);

		return http.build();
	}

	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.roles(Role.ADMIN.name())
				.build();

		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder.encode("user"))
				.roles(Role.USER.name())
				.build();

		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(admin, user);

		return userDetailsManager;
	}

}