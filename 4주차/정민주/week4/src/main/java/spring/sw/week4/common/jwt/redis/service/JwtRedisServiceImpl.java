package spring.sw.week4.common.jwt.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.sw.week4.common.exception.collections.business.jwt.RefreshTokenExpirationException;
import spring.sw.week4.common.jwt.redis.model.RedisRefreshToken;
import spring.sw.week4.common.jwt.redis.repository.RedisRefreshTokenRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRedisServiceImpl implements JwtRedisService {

    private final RedisRefreshTokenRepository refreshTokenRepository;
    @Override
    public RedisRefreshToken save(RedisRefreshToken redisRefreshTokenDto) {
       return refreshTokenRepository.save(redisRefreshTokenDto);
    }

    @Override
    public RedisRefreshToken findById(String id) {
        return refreshTokenRepository.findById(id)
                .orElseThrow(() -> new RefreshTokenExpirationException());
    }

}
