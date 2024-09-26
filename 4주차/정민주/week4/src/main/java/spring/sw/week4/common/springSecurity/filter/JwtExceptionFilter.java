package spring.sw.week4.common.springSecurity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.sw.week4.common.exception.collections.business.jwt.TokenMissingException;
import spring.sw.week4.common.exception.dto.ExceptionDto;
import spring.sw.week4.common.exception.message.jwt.TokenExceptionMessage;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (TokenMissingException e) {
            writeJsonToResponse(
                    response,
                    TokenExceptionMessage.TokenMissingInHeader,
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (SecurityException e) { // 비밀키 일치 X 처리
            writeJsonToResponse(
                    response,
                    TokenExceptionMessage.SignatureNotMatch,
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (ExpiredJwtException e) { //토큰 기간 만료시 처리
            writeJsonToResponse(
                    response,
                    TokenExceptionMessage.JwtAccessTokenExpired,
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (UsernameNotFoundException e) { //해당 사용자 이메일의 계정이 존재하지 않는 경우
            writeJsonToResponse(
                    response,
                    "USER NOT FOUND",
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (JwtException e) { // 토큰 자체가 잘못된 경우
            writeJsonToResponse(
                    response,
                    TokenExceptionMessage.InvalidJwtToken,
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (Exception e) {
            throw e;
        }
    }

    public void writeJsonToResponse( // 반환 할 HTTP Response 를 설정
                                     HttpServletResponse httpServletResponse, //HTTP 응답
                                     String errorMessage, //에러 메시지
                                     Integer statusCode //상태 코드
    ) throws IOException {
        ExceptionDto expireExceptionDto = ExceptionDto
                .builder()
                .success("false")
                .errorMessage(errorMessage)
                .occurredTime(LocalDateTime.now())
                .statusCode(statusCode)
                .build();

        // Content Type 설정
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        // HTTP 응답 상태 코드 설정
        httpServletResponse.setStatus(statusCode);

        // ExceptionDto 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 날짜/시간을 문자열로 직렬화
        String jsonResponse = objectMapper.writeValueAsString(expireExceptionDto);

        // JSON 응답 작성
        httpServletResponse.getWriter().write(jsonResponse);
    }
}
