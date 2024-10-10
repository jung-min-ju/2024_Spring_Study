package com.example.filemanage.fileMetaData;

import com.example.filemanage.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileMetaDataExceptionCode implements ExceptionCode {
    FILE_NOT_FOUND(404,"파일을 찾을 수 없습니다."),
    ACCESS_DENIED(403, "권한이 없습니다."),
    FILE_DOWNLOAD_FAILED(500,"파일 다운로드 실패");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
