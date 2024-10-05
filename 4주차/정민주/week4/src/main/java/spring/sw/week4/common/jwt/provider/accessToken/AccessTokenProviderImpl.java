package spring.sw.week4.common.jwt.provider.accessToken;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.sw.week4.common.config.constant.JwtConstants;
import spring.sw.week4.common.config.spring.JwtProps;
import spring.sw.week4.common.jwt.dto.AccessTokenDto;
import spring.sw.week4.common.jwt.provider.AbstractTokenProvider;
import spring.sw.week4.common.jwt.redis.service.JwtRedisService;

import java.util.Date;

@Component
@Slf4j
public class AccessTokenProviderImpl extends AbstractTokenProvider implements AccessTokenProvider {
    private JwtRedisService jwtRedisService;

    public AccessTokenProviderImpl(JwtProps jwtProps, JwtRedisService jwtRedisService) {
        super(jwtProps);
        this.jwtRedisService = jwtRedisService;
    }

    public AccessTokenDto createAccessToken(String id, String roles) {
        JwtBuilder token = Jwts.builder();

        Date now = new Date();
        Date accessExpiredTime = new Date(now.getTime() + getAccessTokenExpirationMinutes() );

        String jwt = Jwts.builder()
                .header().type(JwtConstants.JWT)
                .and()
                .claims()
                .subject(id)
                .add("role",roles) //추가 클레임
                .issuedAt(now)
                .expiration(accessExpiredTime)
                .and()
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();

        return AccessTokenDto.builder()
                .accessToken(jwt)
                .accessTokenExpiredTime(accessExpiredTime)
                .build();

    }

}
