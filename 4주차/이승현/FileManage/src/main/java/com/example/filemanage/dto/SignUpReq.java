package com.example.filemanage.dto;


import com.example.filemanage.user.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpReq(
        @NotBlank(message = "닉네임은 필수 입력입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        String username,

        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 업력입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "비밀번호는 8~16자 영문과 숫자를 사용하세요.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "비밀번호는 8~16자 영문과 숫자를 사용하세요.")
        String passwordCheck,

        @NotBlank(message = "전화번호는 필수 입력입니다.")
        @Pattern(regexp = "^\\d{9,11}", message = "전화번호는 -을 제외한 숫자만 입력해주세요.")
        String phoneNumber
) {
    public Users toEntity(String password) {
        return Users.builder()
                .username(username)
                .email(email)
                .password(password)
                .PhoneNumber(phoneNumber)
                .build();
    }
}
