package spring.sw.week4.common.springSecurity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static spring.sw.week4.common.exception.message.business.AuthExceptionMessage.UnAuthenticatedUserAccess;


@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    // AuthenticationEntryPoint는 인증되지 않은 사용자가 인증이 필요한 요청 엔드포인트로 접근하려 할 때, 예외를 핸들링 할 수 있도록 도와준다.
    // 인증되지 않은 사용자의 경우를 JwtdoFilter에서 이미 구현했지만, 혹시 놓친 경우를 위해 넣었다.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(UnAuthenticatedUserAccess);
    }

}
