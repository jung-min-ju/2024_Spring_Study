package com.example.filemanage.common.exception;

public record ExceptionRes(
        int status,
        String message
) {
    public static ExceptionRes of(int status, String message) {
        return new ExceptionRes(status, message);
    }
}
