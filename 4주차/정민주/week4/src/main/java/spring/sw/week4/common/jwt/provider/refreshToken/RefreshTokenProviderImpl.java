package spring.sw.week4.common.jwt.provider.refreshToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.sw.week4.common.config.constant.JwtConstants;
import spring.sw.week4.common.config.spring.JwtProps;
import spring.sw.week4.common.jwt.dto.RefreshTokenDto;
import spring.sw.week4.common.jwt.dto.TokenSubAndRoleDto;
import spring.sw.week4.common.jwt.provider.AbstractTokenProvider;
import spring.sw.week4.common.jwt.redis.model.RedisRefreshToken;
import spring.sw.week4.common.jwt.redis.service.JwtRedisService;

import java.util.Date;

@Component
@Slf4j
public class RefreshTokenProviderImpl extends AbstractTokenProvider implements RefreshTokenProvider {
    private JwtRedisService jwtRedisService;

    public RefreshTokenProviderImpl(JwtProps jwtProps, JwtRedisService jwtRedisService) {
        super(jwtProps);
        this.jwtRedisService = jwtRedisService;
    }

    public RefreshTokenDto createRefreshToken(String id, String role) {
        JwtBuilder token = Jwts.builder();

        Date now = new Date();
        Date refreshExpiredTime = new Date(now.getTime() + getRefreshTokenExpirationMinutes() );

        String jwt = Jwts.builder()
                .header().type(JwtConstants.JWT)
                .and()
                .claims()
                .subject(id)
                .add("role",role)
                .issuedAt(now)
                .expiration(refreshExpiredTime)
                .and()
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();

        RedisRefreshToken refreshTokenDto = RedisRefreshToken.builder()
                .id(id)
                .refrehToken(jwt)
                .build();

        jwtRedisService.save(refreshTokenDto);

        return RefreshTokenDto.builder()
                .refreshToken(jwt)
                .refreshTokenExpiredTime(refreshExpiredTime)
                .build();
    }

    public boolean validateRefreshTokenInRedis(String token) {
        //sub와 권한 가져옴
        Claims claims = getClaims(token);
        String sub = claims.getSubject();
        if(jwtRedisService.findById(sub).equals(null)) return false;

        //토큰이 만료된 거라면 false, 아니면 true 반환
        return !claims.getExpiration().before(new Date());
    }

    public TokenSubAndRoleDto getSubjectAndRoleFromRefresh (String token) {

        Claims claims = getClaims(token);
        String sub = claims.getSubject();
        String role = (String) claims.get("role");

        TokenSubAndRoleDto tokenSubAndRoleDto = TokenSubAndRoleDto.builder()
                .accountId(sub)
                .role(role)
                .build();
        //account ID 와 roles 을 반환

        return tokenSubAndRoleDto;
    }


}
