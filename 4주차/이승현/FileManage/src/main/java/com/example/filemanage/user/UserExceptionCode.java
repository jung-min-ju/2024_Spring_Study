package com.example.filemanage.user;

import com.example.filemanage.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    PASSWORD_NOT_CONFIRMED(403, "비밀번호 인증이 완료되지 않았습니다."),
    EMAIL_NOT_CONFIRMED(403, "이메일 인증이 완료되지 않았습니다."),
    PHONE_NOT_CONFIRMED(403, "전화번호 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_EXIST(409, "사용할 수 없는 이메일입니다."),
    PHONE_ALREADY_EXIST(409, "사용할 수 없는 전화번호입니다."),
    INVALID_CONFIRM_CODE(400, "인증번호가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    PASSWORD_INCORRECT(400, "비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
