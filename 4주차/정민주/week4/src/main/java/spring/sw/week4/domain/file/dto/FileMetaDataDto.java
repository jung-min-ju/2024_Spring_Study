package spring.sw.week4.domain.file.dto;

import java.time.LocalDateTime;

public record FileMetaDataDto(
        String bucketName,
        String fileName,       // 변경된 파일 이름
        String filePath,       // S3 URL 또는 파일 경로
        String fileKey,
        Long fileSize,         // 파일 크기
        LocalDateTime uploadTime, // 업로드 시간
        String contentType   // 파일의 MIME 타입
) {}