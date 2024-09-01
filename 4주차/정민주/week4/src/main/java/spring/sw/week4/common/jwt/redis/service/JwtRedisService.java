package spring.sw.week4.common.jwt.redis.service;


import spring.sw.week4.common.jwt.redis.model.RedisRefreshToken;

public interface JwtRedisService {
    //리프레시 토큰을 redis 에 저장
    RedisRefreshToken save(RedisRefreshToken redisRefreshTokenDto);
    //해당 Id에 토큰 있는지 찾아오기
    RedisRefreshToken findById(String id);
}
