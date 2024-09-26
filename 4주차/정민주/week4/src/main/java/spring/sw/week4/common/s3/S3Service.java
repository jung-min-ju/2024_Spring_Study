package spring.sw.week4.common.s3;

import org.springframework.web.multipart.MultipartFile;
import spring.sw.week4.common.exception.collections.io.S3AccessException;
import spring.sw.week4.domain.file.dto.FileMetaDataDto;

public interface S3Service {
    FileMetaDataDto uploadImageToS3(MultipartFile image) throws S3AccessException;
    void  deleteFileFromS3(String fileKey) throws S3AccessException;
}
