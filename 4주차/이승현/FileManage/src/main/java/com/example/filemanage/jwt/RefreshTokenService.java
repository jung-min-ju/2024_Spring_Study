package com.example.filemanage.jwt;

import com.example.filemanage.common.RedisUtil;
import com.example.filemanage.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisUtil redisUtil;
    private final TokenProvider tokenProvider;

    @Value("${token.refresh}")
    private long REFRESH_EXPIRATION;

    public void saveRefreshToken(String username, String refreshToken) {
        String key = username;
        redisUtil.setData(key, refreshToken);
        redisUtil.setDataExpire(key, REFRESH_EXPIRATION);
    }

    public String findRefreshToken(String refreshToken) {
        String username = tokenProvider.getUsername(refreshToken);
        String redisRefreshToken = (String) redisUtil.getData(username);

        if (redisRefreshToken == null) {
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        return redisRefreshToken;
    }

    public void deleteRefreshToken(String refreshToken) {
        String username = tokenProvider.getUsername(refreshToken);
        redisUtil.deleteData(username);
    }

    public String ReIssueAccessToken(String refreshToken) {
        Long userId = tokenProvider.getUserId(refreshToken);
        String username = tokenProvider.getUsername(refreshToken);

        return tokenProvider.getAccessToken(userId, username);
    }

    public String ReIssueRefreshToken(String refreshToken) {
        this.deleteRefreshToken(refreshToken);

        String username = tokenProvider.getUsername(refreshToken);
        Long userId = tokenProvider.getUserId(refreshToken);

        String newRefreshToken = tokenProvider.getRefreshToken(userId, username);
        this.saveRefreshToken(username, newRefreshToken);

        return newRefreshToken;
    }
}
