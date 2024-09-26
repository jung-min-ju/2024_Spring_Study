package spring.sw.week4.common.jwt.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenProvider {
    String resolveToken(HttpServletRequest request);
    Claims getClaims(String token);
}
