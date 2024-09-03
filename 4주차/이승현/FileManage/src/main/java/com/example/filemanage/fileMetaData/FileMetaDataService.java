package com.example.filemanage.fileMetaData;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.filemanage.common.AwsFileService;
import com.example.filemanage.common.exception.CustomException;
import com.example.filemanage.dto.FileRes;
import com.example.filemanage.user.UserExceptionCode;
import com.example.filemanage.user.UserRepository;
import com.example.filemanage.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileMetaDataService {
    private final FileMetaDataRepository fileMetaDataRepository;
    private final UserRepository userRepository;
    private final AwsFileService awsFileService;

    @Transactional
    public void uploadFile(Long userId, MultipartFile imageFiles) throws IOException {
        String fileUrl = awsFileService.saveFile(imageFiles, userId);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

        FileMetaData newFile = FileMetaData.builder()
                .file_name(imageFiles.getOriginalFilename())
                .file_path(fileUrl)
                .file_size(imageFiles.getSize())
                .upload_time(LocalDateTime.now())
                .content_type(imageFiles.getContentType())
                .user(user)
                .build();

        fileMetaDataRepository.save(newFile);
    }

    @Transactional(readOnly = true)
    public Page<FileRes> getMyFiles(Long userId, Integer page) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 20);

        Page<FileMetaData> fileMetaDataList = fileMetaDataRepository.findAllByUser(user, pageable);

        return fileMetaDataList.map(FileRes::fromEntity);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<InputStreamResource> downloadFile(Long userId, Integer fileId) throws IOException {
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(FileMetaDataExceptionCode.FILE_NOT_FOUND));

        if (!Objects.equals(fileMetaData.getUser().getId(), userId)) {
            throw new CustomException(FileMetaDataExceptionCode.ACCESS_DENIED);
        }

        String fileUrl = extractPath(fileMetaData.getFile_path());
        String decodedFileUrl = decodeFileName(fileUrl);


        S3Object s3Object = awsFileService.getS3Object(decodedFileUrl);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename =\"" + fileMetaData.getFile_name() + "\"")
                .contentType(MediaType.parseMediaType(fileMetaData.getContent_type()))
                .body(new InputStreamResource(inputStream));
    }

    private String decodeFileName(String encodedFileName) {
        try {
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일 이름 디코딩 중 오류 발생: " + encodedFileName, e);
        }
    }


    @Transactional
    public void deleteFile(Long userId, Integer fileId) {
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(FileMetaDataExceptionCode.FILE_NOT_FOUND));

        if (!Objects.equals(fileMetaData.getUser().getId(), userId)) {
            throw new CustomException(FileMetaDataExceptionCode.ACCESS_DENIED);
        }

        String decodedFileUrl = decodeFileName(extractPath(fileMetaData.getFile_path()));
        awsFileService.deleteFile(decodedFileUrl);

        fileMetaDataRepository.delete(fileMetaData);
    }

    private static String extractPath(String url) {
        if (url.startsWith("https://springstudybucket.s3.ap-southeast-2.amazonaws.com/")) {
            return url.substring("https://springstudybucket.s3.ap-southeast-2.amazonaws.com/".length());
        } else {
            throw new IllegalArgumentException("URL does not start with the base URL");
        }
    }
}
