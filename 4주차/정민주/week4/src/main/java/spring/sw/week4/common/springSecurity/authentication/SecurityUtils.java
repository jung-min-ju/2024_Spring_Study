package spring.sw.week4.common.springSecurity.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spring.sw.week4.common.exception.collections.business.business.UnAuthenticatedAccessException;

@Component
public class SecurityUtils {

    public Authentication getAuthentication() {
        // SecurityContextHolder에서 인증 정보를 얻어 현재 사용자의 학번, 권한 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.isAuthenticated()){ //인증된 상태가 아닐 때, 에러 처리
            throw new UnAuthenticatedAccessException();
        }
        return authentication;
    }

}
