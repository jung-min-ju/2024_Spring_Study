package com.example.filemanage.jwt;

import com.example.filemanage.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum JwtExceptionCode implements ExceptionCode {
    ACCESS_TOKEN_EXPIRED(401, "Access Token 만료."),
    REFRESH_TOKEN_EXPIRED(401, "Refresh Token 만료."),
    ACCESS_TOKEN_NOT_FOUND(404, "Access Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "Refresh Token을 찾을 수 없습니다."),
    INVALID_ACCESS_TOKEN(401, "잘못된 Access Token.");


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
