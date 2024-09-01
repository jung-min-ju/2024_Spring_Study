package spring.sw.week4.common.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import spring.sw.week4.common.config.spring.JwtProps;

import javax.crypto.SecretKey;

import static spring.sw.week4.common.config.constant.JwtConstants.BEARER_TYPE;


public abstract class AbstractTokenProvider implements TokenProvider {

    private final SecretKey secretKey;
    private final Long refreshTokenExpirationMinutes;
    private final Long accessTokenExpirationMinutes;

    public AbstractTokenProvider(JwtProps jwtProps) {
        refreshTokenExpirationMinutes = jwtProps.getRefreshTokenExpirationMinutes();
        accessTokenExpirationMinutes = jwtProps.getAccessTokenExpirationMinutes();
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProps.getSecretKey()));
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public Claims getClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token); //페이로드 내의 클레임 객체 파싱

        return claimsJws.getPayload();
    }

    // secretKey에 대한 외부 접근은 제한하고, 필요한 경우 관련 메서드만 제공
    protected SecretKey getSecretKey() {
        return this.secretKey;
    }

    // 만료 시간을 제공하는 메서드
    public Long getRefreshTokenExpirationMinutes() {
        return refreshTokenExpirationMinutes;
    }

    public Long getAccessTokenExpirationMinutes() {
        return accessTokenExpirationMinutes;
    }

}
