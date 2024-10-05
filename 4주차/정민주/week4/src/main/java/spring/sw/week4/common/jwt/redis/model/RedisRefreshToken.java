package spring.sw.week4.common.jwt.redis.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 5) //5일 뒤 자동삭제 :  60 * 60 * 24 * 3
public class RedisRefreshToken {

    @Id
    private String id;
    private String refrehToken;

}

