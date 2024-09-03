package com.example.filemanage.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.filemanage.common.exception.AwsExceptionCode;
import com.example.filemanage.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AwsFileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile, Long userId) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new CustomException(AwsExceptionCode.FILE_CONVERT_FAILED));

        return upload(uploadFile, userId);
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String fileName = System.getProperty("user.home") + "/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File convertFile = new File(fileName);

        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                log.info("파일 변환 성공: " + fileName);
                return Optional.of(convertFile);
            } else {
                log.error("파일 생성 실패: 파일이 이미 존재하거나 권한 문제가 있을 수 있습니다. 파일 이름: " + fileName);
            }
        } catch (IOException e) {
            log.error("파일 변환 중 오류 발생. 파일 이름: " + fileName, e);
            throw e;
        }
        return Optional.empty();
    }

    public S3Object getS3Object(String s3FilePath) {
        return amazonS3Client.getObject(bucket, s3FilePath);
    }
    public void deleteFile(String filePath) {
        try {
            amazonS3Client.deleteObject(bucket, filePath);
            log.info("S3에서 파일 삭제 성공: " + filePath);
        } catch (Exception e) {
            log.error("S3에서 파일 삭제 중 오류 발생. 파일 경로: " + filePath, e);
            throw new CustomException(AwsExceptionCode.FILE_DELETE_FAILED);
        }
    }

    private String upload(File uploadFile, Long userId) {
        String fileName = userId + "/" + UUID.randomUUID() + "_" + uploadFile.getName(); // 인코딩하지 않음
        String uploadImgUrl = putS3(uploadFile, fileName);
        removeLocalFile(uploadFile);
        return uploadImgUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    private void removeLocalFile(File file) {
        if (file.delete()) {
            log.info("로컬 파일이 성공적으로 삭제됨.");
            return;
        }
        log.warn("로컬 파일 삭제 실패.");
    }
}
