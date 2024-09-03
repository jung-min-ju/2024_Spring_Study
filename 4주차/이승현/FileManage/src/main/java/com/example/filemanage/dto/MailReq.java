package com.example.filemanage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailReq(
        @Email
        @NotBlank(message = "메일은 필수 입력입니다.")
        String mail
) {
}
