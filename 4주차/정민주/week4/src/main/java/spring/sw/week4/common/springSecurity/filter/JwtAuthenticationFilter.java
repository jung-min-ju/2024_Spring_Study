package spring.sw.week4.common.springSecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.sw.week4.common.exception.collections.business.jwt.TokenMissingException;
import spring.sw.week4.common.jwt.provider.accessToken.AccessTokenProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI(); //요청된 API 경로

        if (!requiredAuthentication(requestURI)) { //허용 된 URL 요청이라면 토큰 검증없이 진행
            filterChain.doFilter(request, response);
            return;
        }
        String token = accessTokenProvider.resolveToken(request);

        if (token == null) {
            throw new TokenMissingException();
        }

        String subject = accessTokenProvider.getClaims(token).getSubject();
        this.authentication(subject);
        filterChain.doFilter(request, response);
    }

    public void authentication(String identifier) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(identifier);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities()
                )
        );
    }

    private boolean requiredAuthentication(String requestURI) {
        Set<String> openUrlPatterns = new HashSet<>(Arrays.asList(
                "/auth/**",
                "/jwt/**",
                "/ws/**",
                "/static/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/v3/api-docs/swagger-config",
                "/favicon.ico"
        ));

        return openUrlPatterns.stream().noneMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }



}
