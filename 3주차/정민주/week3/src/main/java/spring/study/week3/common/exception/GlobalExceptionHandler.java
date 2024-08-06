package spring.study.week3.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.study.week3.common.exception.collections.BusinessException;
import spring.study.week3.common.exception.dto.ExceptionDto;

import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ExceptionDto> createExceptionResponse(
            String errorMessage,
            HttpStatus httpStatus
    ){

        ExceptionDto exceptionDto = ExceptionDto.builder()
                .success("false") //성공 여부 false
                .errorMessage(errorMessage)
                .occurredTime(LocalDateTime.now()) //현재 시간 Mapping
                .statusCode(httpStatus.value()) //상태코드 Integer 값
                .build(); // 빌드
        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDto> handleBusinessException(BusinessException e) {
        // BusinessException에서 던진 메시지를 받아와 클라이언트에게 반환합니다.
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED //BadRequest가 맞긴 하나 로그인,회원가입 틀렸을 때는 보안상으로 안좋다고 합니다.->에러코드 같이 써야 하는 이유
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionDto> handleIOException(IOException e) {
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handle_RuntimeException(RuntimeException e) {
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handle_Exception(Exception e) {
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
