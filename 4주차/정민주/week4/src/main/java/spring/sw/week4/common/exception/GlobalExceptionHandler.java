package spring.sw.week4.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.sw.week4.common.exception.collections.NotFound.ResourceNotFoundException;
import spring.sw.week4.common.exception.collections.business.BusinessException;
import spring.sw.week4.common.exception.dto.ExceptionDto;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleBusinessException(MethodArgumentNotValidException e) {
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST //클라이언트 reqDto 필드 유효성 에러시, 400 반환
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class) //클라이언트의 값에 해당하는 DB 못 찾을 시 404 반환
    public ResponseEntity<ExceptionDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDto> handleBusinessException(BusinessException e) {
        // BusinessException에서 던진 메시지를 받아와 클라이언트에게 반환합니다.
        return createExceptionResponse(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED //BadRequest가 맞긴 하나 로그인,회원가입 틀렸을 때는 보안상으로 안좋다고 합니다.->에러코드 같이 써야 하는 이유
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
