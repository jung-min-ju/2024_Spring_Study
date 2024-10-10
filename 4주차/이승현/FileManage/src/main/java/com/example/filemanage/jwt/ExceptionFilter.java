package com.example.filemanage.jwt;

import com.example.filemanage.common.exception.ExceptionCode;
import com.example.filemanage.common.exception.ExceptionRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            handleTokenException(response, JwtExceptionCode.INVALID_ACCESS_TOKEN);
        }
    }

    private void handleTokenException(HttpServletResponse response, ExceptionCode exceptionCode) throws  IOException {
        ExceptionRes exceptionRes = ExceptionRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());
        String message = objectMapper.writeValueAsString(exceptionRes);

        log.error("Error occurred: {}", exceptionCode.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(message);
    }
}
