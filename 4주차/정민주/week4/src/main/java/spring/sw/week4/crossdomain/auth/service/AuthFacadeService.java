package spring.sw.week4.crossdomain.auth.service;


import spring.sw.week4.common.jwt.dto.AllJwtTokenDto;
import spring.sw.week4.crossdomain.auth.dto.CheckPasswordReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignInReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignUpReqDto;

public interface AuthFacadeService {
    void signUp(SignUpReqDto signUpDto);
    AllJwtTokenDto signIn(SignInReqDto signInReqDto);
    void checkPassword(CheckPasswordReqDto checkPasswordReqDto);
}
