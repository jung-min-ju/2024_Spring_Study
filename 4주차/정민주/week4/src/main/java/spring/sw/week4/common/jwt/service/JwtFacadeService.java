package spring.sw.week4.common.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import spring.sw.week4.common.jwt.dto.AccessTokenDto;
import spring.sw.week4.common.jwt.dto.AllJwtTokenDto;
import spring.sw.week4.domain.user.model.User;

public interface JwtFacadeService {
    AccessTokenDto updateAccessToken(HttpServletRequest request);
    AllJwtTokenDto createAllJwtToken(User user);
}

