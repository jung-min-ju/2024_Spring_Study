package spring.sw.week4.crossdomain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sw.week4.common.exception.collections.business.business.PasswordNotMatchException;
import spring.sw.week4.common.jwt.dto.AllJwtTokenDto;
import spring.sw.week4.common.jwt.service.JwtFacadeService;
import spring.sw.week4.common.util.UtilService;
import spring.sw.week4.crossdomain.auth.dto.CheckPasswordReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignInReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignUpReqDto;
import spring.sw.week4.crossdomain.auth.localCache.LocalCache;
import spring.sw.week4.domain.user.model.User;
import spring.sw.week4.domain.user.service.UserService;

import static spring.sw.week4.common.config.constant.InitConstants.completeVerification;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthFacadeServiceImpl implements AuthFacadeService {
    private final UserService userService;
    private final LocalCache localCache;
    private final UtilService utilService;
    private final JwtFacadeService jwtFacadeService;

    @Override
    public void signUp (SignUpReqDto signUpDto) {
        //1. 현재 받아온 이메일, 전화번호, 비밀번호 검증
        localCache.isVerified(localCache.getEmailCache(), signUpDto.email());
        localCache.isVerified(localCache.getPhoneCache(), signUpDto.phoneNum());
        localCache.isVerified(localCache.getPasswordCache(), signUpDto.password());

        userService.createUser(signUpDto);
    }

    //todo : 위 2번부터 3번 로직은 DataBaseFacade 클래스로 묶어서 빼고, dto 변환 부분 역할만 하는 서비스 클래스 하나 묶어서 나누면 더 좋을 거 같음 -> 일단 패스

    @Override
    public AllJwtTokenDto signIn(SignInReqDto signInReqDto) {
        //1. 유저 존재 확인 및 비밀번호 일치 확인
        User user = userService.signIn(signInReqDto);
        //2. 토큰 생성
        return jwtFacadeService.createAllJwtToken(user);
    }

    @Override
    public void checkPassword(CheckPasswordReqDto checkPasswordReqDto) {
        //비밀 번호 일치 여부 확인
        if(!utilService.checkPassword(checkPasswordReqDto)) {
             throw new PasswordNotMatchException();
        }
        //로컬 캐시에 저장
        localCache.storeVerificationCode(localCache.getPasswordCache(), checkPasswordReqDto.password(), completeVerification);
    }
}
