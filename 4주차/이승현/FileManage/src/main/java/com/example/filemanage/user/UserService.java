package com.example.filemanage.user;

import com.example.filemanage.common.RedisUtil;
import com.example.filemanage.common.SmsUtil;
import com.example.filemanage.common.exception.CustomException;
import com.example.filemanage.dto.PasswordReq;
import com.example.filemanage.dto.SignInReq;
import com.example.filemanage.dto.SignUpReq;
import com.example.filemanage.dto.TokenRes;
import com.example.filemanage.jwt.RefreshTokenService;
import com.example.filemanage.jwt.TokenProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final JavaMailSender javaMailSender;
    private final RefreshTokenService refreshTokenService;
    private final SmsUtil smsUtil;

    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String sender;

    @Transactional
    public void signUp(SignUpReq signUpReq) {
        Object passwdStatus = redisUtil.getData(signUpReq.password());
        Object emailStatus = redisUtil.getData(signUpReq.email());
        Object phoneStatus = redisUtil.getData(signUpReq.phoneNumber());

        if (passwdStatus == null || !passwdStatus.equals("true")) {
            throw new CustomException(UserExceptionCode.PASSWORD_NOT_CONFIRMED);
        }

        if (emailStatus == null || !emailStatus.equals("true")) {
            throw new CustomException(UserExceptionCode.EMAIL_NOT_CONFIRMED);
        }

        if (phoneStatus == null || !phoneStatus.equals("true")) {
            throw new CustomException(UserExceptionCode.PHONE_NOT_CONFIRMED);
        }


        Users users = signUpReq.toEntity(passwordEncoder.encode(signUpReq.password()));

        redisUtil.deleteData(signUpReq.email());
        redisUtil.deleteData(signUpReq.phoneNumber());

        userRepository.save(users);
    }

    @Transactional(readOnly = true)
    public TokenRes signIn(SignInReq signInReq) {
        Users users = userRepository.findByUsername(signInReq.username())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(signInReq.password(), users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return getToken(users);
    }

    public void checkPassword(PasswordReq passwordReq) {
        if (!passwordReq.password().equals(passwordReq.passwordCheck())) {
            throw new CustomException(UserExceptionCode.PASSWORD_INCORRECT);
        }
        redisUtil.setData(passwordReq.password(), "true");
        redisUtil.setDataExpire(passwordReq.password(), 60 * 10L);
    }


    public void sendMail(String receiver) {
        if (userRepository.existsByEmail(receiver)) {
            throw new CustomException(UserExceptionCode.EMAIL_ALREADY_EXIST);
        }

        log.info("메일 전송");
        String code = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(sender);
            message.setRecipients(MimeMessage.RecipientType.TO, receiver);
            message.setSubject("회원가입 인증번호");
            String body = "";
            body += "<h3>" + "회원가입을 위한 인증번호입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            log.warn("메일 전송 실패");
            e.printStackTrace();
        }

        redisUtil.setData(code, receiver);
        redisUtil.setDataExpire(code, 60 * 5L);

        javaMailSender.send(message);
        log.info("메일 전송 완료");
    }

    public boolean validateMail(String mail, String code) {
        if (userRepository.existsByEmail(mail)) {
            throw new CustomException(UserExceptionCode.EMAIL_ALREADY_EXIST);
        }
        log.info("메일 인증 시작");

        if (redisUtil.existed(code) && redisUtil.getData(code).equals(mail)) {
            redisUtil.deleteData(code);
            redisUtil.setData(mail, "true");
            redisUtil.setDataExpire(mail, 60 * 10L);
            log.info("메일 인증 성공");
            return true;
        }
        log.warn("인증 정보 만료");
        throw new CustomException(UserExceptionCode.INVALID_CONFIRM_CODE);
    }

    public void sendSms(String receiver) {
        if (userRepository.existsByPhoneNumber(receiver)) {
            throw new CustomException(UserExceptionCode.PHONE_ALREADY_EXIST);
        }

        log.info("문자 인증 전송 시작");
        String code = createCode();

        redisUtil.setData(code, receiver);
        redisUtil.setDataExpire(code, 60 * 5L);

        smsUtil.sendOne(receiver, code);

        log.info("문자 전송 성공");
    }

    public boolean validateSms(String receiver, String code) {
        if (userRepository.existsByPhoneNumber(receiver)) {
            throw new CustomException(UserExceptionCode.PHONE_ALREADY_EXIST);
        }

        log.info("문자 인증 시작");

        if (redisUtil.existed(code) && redisUtil.getData(code).equals(receiver)) {
            redisUtil.deleteData(code);
            redisUtil.setData(receiver, "true");
            redisUtil.setDataExpire(receiver, 60 * 10L);
            log.info("문자 인증 성공");
            return true;
        }

        log.warn("인증 정보 만료");
        throw new CustomException(UserExceptionCode.INVALID_CONFIRM_CODE);
    }

    private TokenRes getToken(Users users) {
        Long userId = users.getId();
        String username = users.getUsername();

        String accessToken = "Bearer " + tokenProvider.getAccessToken(userId, username);
        String refreshToken = tokenProvider.getRefreshToken(userId, username);

        refreshTokenService.saveRefreshToken(username, refreshToken);

        return TokenRes.of(accessToken, refreshToken);
    }

    public TokenRes updateAccessToken(String refreshTokenInCookie) {
        String refreshToken = refreshTokenService.findRefreshToken(refreshTokenInCookie);
        String newAccessToken = "Bearer " + refreshTokenService.ReIssueAccessToken(refreshToken);

        String newRefreshToken = refreshTokenService.ReIssueRefreshToken(refreshToken);

        return TokenRes.of(newAccessToken, newRefreshToken);
    }

    private String createCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
