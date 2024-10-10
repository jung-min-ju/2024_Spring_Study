package com.example.filemanage.jwt;

import com.example.filemanage.common.exception.CustomException;
import com.example.filemanage.common.exception.ExceptionCode;
import com.example.filemanage.common.exception.ExceptionRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenHeader = request.getHeader("Authorization");

        if (accessTokenHeader == null || !accessTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = accessTokenHeader.substring("Bearer ".length()).trim();

        try {
            tokenProvider.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            handleExceptionToken(response, JwtExceptionCode.ACCESS_TOKEN_EXPIRED);
            return;
        }

        String username = tokenProvider.getUsername(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private void handleExceptionToken(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {

        ExceptionRes exceptionsRes = ExceptionRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());
        String messageBody = objectMapper.writeValueAsString(exceptionsRes);

        log.error("Error occurred: {}", exceptionCode.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(messageBody);
    }
}
