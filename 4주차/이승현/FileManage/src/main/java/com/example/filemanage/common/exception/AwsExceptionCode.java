package com.example.filemanage.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AwsExceptionCode implements ExceptionCode{

    FILE_CONVERT_FAILED(500, "파일 변환에 실패했습니다."),
    FILE_DOWNLOAD_FAILED(500, "파일 다운로드 실패."),
    FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다."),
    FILE_DELETE_FAILED(500,"파일 삭제 실패.");

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
