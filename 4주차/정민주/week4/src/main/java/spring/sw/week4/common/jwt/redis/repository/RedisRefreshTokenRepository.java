package spring.sw.week4.common.jwt.redis.repository;


import org.springframework.data.repository.CrudRepository;
import spring.sw.week4.common.jwt.redis.model.RedisRefreshToken;

public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshToken, String> {

}

