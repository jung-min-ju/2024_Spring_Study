package spring.sw.week4.common.exception.message.jwt;

public class TokenExceptionMessage {
    public static String TokenMissingInHeader = "헤더에 토큰이 포함되어 있지 않습니다.";
    public static final String SignatureNotMatch = "시크릿 키가 동일하지 않습니다.";
    public static final String JwtAccessTokenExpired = "액세스 토큰이 만료되었습니다.";
    public static String RefreshTokenExpired = "만료된 리프래시 토큰입니다.";
    public static final String UndefinedException = "정의되지 않은 예외 처리입니다.";
    public static final String InvalidJwtToken =  "입력하신 이메일의 계정이 존재하지 않습니다.";
}