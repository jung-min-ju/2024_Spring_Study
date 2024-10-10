package com.example.filemanage.user;

import com.example.filemanage.common.CookieUtil;
import com.example.filemanage.common.SuccessRes;
import com.example.filemanage.common.exception.CustomException;
import com.example.filemanage.dto.*;
import com.example.filemanage.jwt.JwtExceptionCode;
import com.example.filemanage.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq) {

        userService.signUp(signUpReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("성공적으로 가입되었습니다."));
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {
        TokenRes tokenRes = userService.signIn(signInReq);

        return createTokenRes(tokenRes);
    }

    @GetMapping("/update/access/token")
    public ResponseEntity<?> updateAccessToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenRes tokenRes = userService.updateAccessToken(refreshToken);

        return createTokenRes(tokenRes);
    }

    @PostMapping("/password/check")
    public ResponseEntity<?> checkPassword(@Valid @RequestBody PasswordReq passwordReq) {

        userService.checkPassword(passwordReq);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("비밀번호 일치."));
    }

    @PostMapping("/email/send")
    public ResponseEntity<?> sendMail(@Valid @RequestBody MailReq mailReq) {

        userService.sendMail(mailReq.mail());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("메일 전송 완료."));
    }

    @PostMapping("/email/check")
    private ResponseEntity<?> validateMail(@Valid @RequestBody ValidateMailReq validateMailReq) {

        userService.validateMail(validateMailReq.mail(), validateMailReq.code());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("메일 인증 성공."));
    }

    @PostMapping("/sms/send")
    public ResponseEntity<?> sendSms(@Valid @RequestBody SmsReq smsReq) {

        userService.sendSms(smsReq.phone_number());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("문자 전송 완료."));
    }

    @PostMapping("/sms/check")
    public ResponseEntity<?> validateSms(@Valid @RequestBody ValidateSmsReq validateSmsReq) {

        userService.validateSms(validateSmsReq.phone_number(), validateSmsReq.code());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessRes.from("문자 인증 성공."));
    }

    private ResponseEntity<?> createTokenRes(TokenRes tokenRes) {

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", tokenRes.accessToken());

        return ResponseEntity.ok()
                .header("Set-Cookie",
                        cookieUtil.createCookie("refresh_token", tokenRes.refreshToken()).toString())
                .body(responseData);
    }



    @PostMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
