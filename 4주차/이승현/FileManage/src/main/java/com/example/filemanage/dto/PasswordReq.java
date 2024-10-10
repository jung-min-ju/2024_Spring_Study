package com.example.filemanage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordReq(
        @NotBlank(message = "비밀번호는 필수 업력입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "비밀번호는 8~16자 영문과 숫자를 사용하세요.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 업력입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "비밀번호는 8~16자 영문과 숫자를 사용하세요.")
        String passwordCheck
) {
}
