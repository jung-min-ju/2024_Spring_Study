package com.example.filemanage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ValidateSmsReq(
        @NotBlank(message = "전화번호는 필수 입력입니다.")
        @Pattern(regexp = "^\\d{9,11}", message = "전화번호는 -을 제외한 숫자만 입력해주세요.")
        String phone_number,

        @NotBlank(message = "인증번호는 필수 입력입니다.")
        String code
) {
}
