package spring.sw.week4.common.exception.message.business;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthExceptionMessage {
    public static final String UnAuthenticatedUserAccess = "인증되지 않은 사용자입니다.";
    public static final String UserNotFoundException = "존재하지 않는 유저 입니다.";
    public static final String PasswordNotMatch = "입력하신 Password가 일치하지 않습니다.";
    public static final String UserAlreadyExists = "이미 존재 하는 유저 입니다.";
}
