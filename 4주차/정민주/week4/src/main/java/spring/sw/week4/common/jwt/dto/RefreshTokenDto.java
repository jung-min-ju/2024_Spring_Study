package spring.sw.week4.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {
    private String refreshToken;
    //refresh 토큰
    private Date refreshTokenExpiredTime;
    //refresh 토큰 만료 시간
}
