package spring.sw.week4.crossdomain.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import spring.sw.week4.common.config.constant.ControllerConstants;
import spring.sw.week4.common.exception.collections.io.BindingErrors;
import spring.sw.week4.common.jwt.dto.AllJwtTokenDto;
import spring.sw.week4.crossdomain.auth.dto.CheckPasswordReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignInReqDto;
import spring.sw.week4.crossdomain.auth.dto.SignUpReqDto;
import spring.sw.week4.crossdomain.auth.service.AuthFacadeService;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthFacadeService authFacadeService;

    @PostMapping("/signup")
    public ResponseEntity<?> authSignup(@RequestBody @Valid SignUpReqDto SignUpDto, BindingResult bindingResult ) throws IOException {
        //@Valid 체크
        handleBindingErrors(bindingResult);
        authFacadeService.signUp(SignUpDto);
        return new ResponseEntity<>(ControllerConstants.completeSignUp, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authSignIn(@RequestBody @Valid SignInReqDto signInDto, BindingResult bindingResult) {
        // BindingResult 에러 처리
        handleBindingErrors(bindingResult);

        AllJwtTokenDto allJwtTokenDto = authFacadeService.signIn(signInDto);
        return ResponseEntity.ok(allJwtTokenDto);
    }

    @PostMapping("/check/password")
    public ResponseEntity<?> checkPassword(@RequestBody @Valid CheckPasswordReqDto checkPasswordReqDto, BindingResult bindingResult) {
        // BindingResult 에러 처리
        handleBindingErrors(bindingResult);

        authFacadeService.checkPassword(checkPasswordReqDto);
        return ResponseEntity.ok(ControllerConstants.checkPassword);
    }

    public void handleBindingErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BindingErrors();
        }
    }

}
