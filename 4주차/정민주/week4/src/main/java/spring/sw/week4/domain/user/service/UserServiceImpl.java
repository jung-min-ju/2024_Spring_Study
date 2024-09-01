package spring.sw.week4.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sw.week4.common.exception.collections.NotFound.UserNotFoundException;
import spring.sw.week4.common.exception.collections.business.business.PasswordNotMatchException;
import spring.sw.week4.common.exception.collections.business.database.DuplicateUniqueKeyException;
import spring.sw.week4.common.springSecurity.authentication.SecurityUtils;
import spring.sw.week4.crossdomain.auth.dto.SignInReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignUpReqDto;
import spring.sw.week4.domain.user.enums.Role;
import spring.sw.week4.domain.user.model.User;
import spring.sw.week4.domain.user.repository.UserRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Override
    public void createUser(SignUpReqDto signUpReqDto) {

        String encodePassword = passwordEncoder.encode(signUpReqDto.password());

        User user = User.builder()
                .name(signUpReqDto.name())
                .password(encodePassword)
                .email(signUpReqDto.email())
                .phoneNum(signUpReqDto.phoneNum())
                .role(Role.USER)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) { //unique 키 중복시 발생 시킬 오류
            throw new DuplicateUniqueKeyException();
        }

    }

    @Override
    public User signIn(SignInReqDto signInReqDto) {
        User user = userRepository.findByEmail(signInReqDto.email())
                .orElseThrow(()-> new UserNotFoundException());

        if(!passwordEncoder.matches(signInReqDto.password(), user.getPassword())){
            throw new PasswordNotMatchException();
        }

        return user;
    }


    @Override
    public User findNowUser() {
        Authentication authentication = securityUtils.getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException());
    }

}
