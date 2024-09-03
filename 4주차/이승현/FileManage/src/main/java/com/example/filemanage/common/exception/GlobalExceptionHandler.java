package com.example.filemanage.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        ExceptionRes exceptionRes = ExceptionRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());
        log.error("에러 발생 : {}, {}", e.getMessage(), getCustomStackTrace(e));
        return ResponseEntity.status(exceptionRes.status()).body(exceptionRes);
    }

    @ExceptionHandler
    protected ResponseEntity<?> customServerException(Exception ex) {

        ExceptionCode exceptionCode = GlobalExceptionCode.INTERNAL_SERVER_ERROR;
        ExceptionRes exceptionsRes = ExceptionRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());
        log.error("Error occurred : {}, Stack trace: {}", ex.getMessage(), getCustomStackTrace(ex));
        return ResponseEntity.status(exceptionsRes.status()).body(exceptionsRes);
    }

    /* 오류 코드 5줄 불러오기 */
    public String getCustomStackTrace(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(stackTrace.length, 5); i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
