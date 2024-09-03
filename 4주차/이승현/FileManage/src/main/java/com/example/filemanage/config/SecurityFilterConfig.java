package com.example.filemanage.config;

import com.example.filemanage.jwt.AuthenticationFilter;
import com.example.filemanage.jwt.ExceptionFilter;
import com.example.filemanage.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(userDetailsService, tokenProvider, objectMapper);
    }

    @Bean
    public ExceptionFilter exceptionFilter() {
        return new ExceptionFilter(objectMapper);
    }
}
