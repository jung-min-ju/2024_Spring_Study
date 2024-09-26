package spring.sw.week4.domain.user.service;


import spring.sw.week4.crossdomain.auth.dto.SignInReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignUpReqDto;
import spring.sw.week4.domain.user.model.User;

public interface UserService {
    void createUser(SignUpReqDto signUpReqDto);
    User signIn(SignInReqDto signInReqDto);
    User findNowUser();
}