package spring.sw.week4.domain.file.service;

import org.springframework.web.multipart.MultipartFile;
import spring.sw.week4.common.exception.collections.io.S3AccessException;
import spring.sw.week4.common.exception.collections.io.NotMatchUserToFileOwner;
import spring.sw.week4.domain.file.dto.GetFileResDto;
import spring.sw.week4.domain.file.dto.FileDownloadResDto;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void uploadFile(MultipartFile multipartFile) throws S3AccessException;
    List<GetFileResDto> getFiles();
    FileDownloadResDto downloadFile(Integer id) throws IOException;
    void deleteFile(Integer id) throws S3AccessException;
}
