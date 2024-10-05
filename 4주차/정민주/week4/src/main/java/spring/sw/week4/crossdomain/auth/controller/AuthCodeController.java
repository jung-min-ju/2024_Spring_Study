package spring.sw.week4.crossdomain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.sw.week4.common.exception.collections.io.BindingErrors;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneReqDto;
import spring.sw.week4.crossdomain.auth.smtp.service.email.EmailService;
import spring.sw.week4.crossdomain.auth.smtp.service.phone.PhoneNumService;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth/code")
@RequiredArgsConstructor
@Slf4j
public class AuthCodeController {
    private final EmailService emailService;
    private final PhoneNumService phoneNumService;

    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmailCode(@Valid @RequestBody EmailReqDto emailReqDto, BindingResult bindingResult) throws MessagingException {
        handleBindingErrors(bindingResult);

        emailService.sendVerifyNumberByEmail(emailReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/check/email")
    public ResponseEntity<?> validateEmailCode(@Valid @RequestBody EmailAuthCodeReqDto emailAuthCodeReqDto, BindingResult bindingResult) {
        handleBindingErrors(bindingResult);

        emailService.checkVerifyNumberByEmail(emailAuthCodeReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send/phone")
    public ResponseEntity<?> sendPhoneNumCode(@Valid @RequestBody PhoneReqDto phoneReqDto, BindingResult bindingResult) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        handleBindingErrors(bindingResult);

        phoneNumService.sendVerifyNumberByPhoneNum(phoneReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/check/phone")
    public ResponseEntity<?> validatePhoneNumCode(@Valid @RequestBody PhoneAuthCodeReqDto phoneAuthCodeReqDto, BindingResult bindingResult) {
        handleBindingErrors(bindingResult);

        phoneNumService.checkVerifyNumberByPhoneNum(phoneAuthCodeReqDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void handleBindingErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BindingErrors();
        }
    }

}
