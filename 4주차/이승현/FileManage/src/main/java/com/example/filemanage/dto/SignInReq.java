package com.example.filemanage.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInReq(
        @NotBlank(message = "아이디는 필수 입력입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password
) {
}
