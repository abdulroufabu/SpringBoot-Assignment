package com.test.statement.config;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.statement.utils.AuthorizationUtil;
import com.test.statement.utils.AuthorizationUtil.Role;
import com.test.statement.utils.SearchInput;

@Component
public class AccountInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AccountInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("Request URL::" + request.getRequestURL().toString() 
				+ ":: Start Time=" + System.currentTimeMillis());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			response.addHeader("Interceptor", "Security authentication not exists!");
			logger.warn("Security authentication not exists.!");
			return false;
		}
		
		List<Role> roles = auth.getAuthorities().stream()
				.map(e -> Role.valueOf(e.getAuthority().substring(5)))
				.collect(Collectors.toList());

		String jsonBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		logger.info("Interceptor gets body content:" + jsonBody);

		ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
		SearchInput inputParam = mapper.readValue(jsonBody, new TypeReference<SearchInput>() {
		});

		// Validate the authorization of roles and the request.
		AuthorizationUtil.validateInputRequest(inputParam, roles);

		return true;

	}


}
