package com.example.filemanage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValidateMailReq(
        @NotBlank(message = "메일은 필수 입력입니다.")
        @Email
        String mail,

        @NotBlank(message = "인증번호는 필수 입력입니다.")
        String code
) {
}
