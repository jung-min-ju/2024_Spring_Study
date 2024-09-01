package spring.sw.week4.common.jwt.provider.accessToken;

import spring.sw.week4.common.jwt.dto.AccessTokenDto;
import spring.sw.week4.common.jwt.provider.TokenProvider;

public interface AccessTokenProvider extends TokenProvider {
    AccessTokenDto createAccessToken(String id, String roles);
}
