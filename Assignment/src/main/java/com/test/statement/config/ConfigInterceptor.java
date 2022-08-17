package com.test.statement.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ConfigInterceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	AccountInterceptor inperceptor = new AccountInterceptor();
        registry.addInterceptor(inperceptor).addPathPatterns("/api/v1/**");
    }

}