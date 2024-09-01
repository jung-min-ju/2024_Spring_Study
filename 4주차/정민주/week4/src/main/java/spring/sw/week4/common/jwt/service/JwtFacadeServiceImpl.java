package spring.sw.week4.common.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.sw.week4.common.exception.collections.business.jwt.RefreshTokenExpirationException;
import spring.sw.week4.common.exception.collections.business.jwt.TokenMissingException;
import spring.sw.week4.common.jwt.dto.AccessTokenDto;
import spring.sw.week4.common.jwt.dto.AllJwtTokenDto;
import spring.sw.week4.common.jwt.dto.RefreshTokenDto;
import spring.sw.week4.common.jwt.dto.TokenSubAndRoleDto;
import spring.sw.week4.common.jwt.provider.accessToken.AccessTokenProvider;
import spring.sw.week4.common.jwt.provider.refreshToken.RefreshTokenProvider;
import spring.sw.week4.domain.user.model.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtFacadeServiceImpl implements JwtFacadeService {
    private final RefreshTokenProvider refreshTokenProvider;
    private final AccessTokenProvider accessTokenProvider;

    @Override
    public AccessTokenDto updateAccessToken(HttpServletRequest request) {
        //리프래시 토큰이 있는지 검증
        String refreshToken = refreshTokenProvider.resolveToken(request);
        if(refreshToken.equals(null)) throw new TokenMissingException();

        boolean isRefreshTokenValid = refreshTokenProvider.validateRefreshTokenInRedis(refreshToken);

        //해당 토큰이 레디스에 존재하는지 + 토큰 자체의 유효성 검증
        if (!isRefreshTokenValid) throw new RefreshTokenExpirationException();

        TokenSubAndRoleDto tokenSubAndRoleDto = refreshTokenProvider.getSubjectAndRoleFromRefresh(refreshToken);
        return accessTokenProvider.createAccessToken(tokenSubAndRoleDto.getAccountId(), tokenSubAndRoleDto.getRole());
    }

    @Override
    public AllJwtTokenDto createAllJwtToken(User user) {
        RefreshTokenDto refreshTokenDto = refreshTokenProvider.createRefreshToken
                (user.getEmail(), user.getRole().getRoleName());

        AccessTokenDto accessTokenDto = accessTokenProvider.createAccessToken
                (user.getEmail(), user.getRole().getRoleName());

        AllJwtTokenDto allJwtTokenAndNickDto = AllJwtTokenDto.builder()
                .accessTokenDto(accessTokenDto)
                .refreshTokenDto(refreshTokenDto)
                .build();

        return allJwtTokenAndNickDto;
    }

}
