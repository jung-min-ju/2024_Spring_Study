package spring.sw.week4.common.jwt.provider.refreshToken;

import spring.sw.week4.common.jwt.dto.RefreshTokenDto;
import spring.sw.week4.common.jwt.dto.TokenSubAndRoleDto;
import spring.sw.week4.common.jwt.provider.TokenProvider;

public interface RefreshTokenProvider extends TokenProvider {
    RefreshTokenDto createRefreshToken(String id, String roles );
    boolean validateRefreshTokenInRedis(String token);
    TokenSubAndRoleDto getSubjectAndRoleFromRefresh (String token);
}
