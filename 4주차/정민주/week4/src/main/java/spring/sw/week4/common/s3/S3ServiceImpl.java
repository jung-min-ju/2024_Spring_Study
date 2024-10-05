package spring.sw.week4.common.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.sw.week4.common.config.spring.S3Config;
import spring.sw.week4.common.exception.collections.io.S3AccessException;
import spring.sw.week4.common.util.UtilService;
import spring.sw.week4.domain.file.dto.FileMetaDataDto;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class S3ServiceImpl implements S3Service{
    private final UtilService utilService;
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    @Override
    public FileMetaDataDto uploadImageToS3 (MultipartFile file) throws S3AccessException {
        String bucketName = s3Config.getBucketName();
        String originName = file.getOriginalFilename(); //원본 이미지 이름
        String ext = originName.substring(originName.lastIndexOf(".")); //확장자
        String fileKey = utilService.getRandomUUID(originName); //새로 생성된 이미지 이름
        ObjectMetadata metadata = new ObjectMetadata(); //메타데이터

        metadata.setContentType(file.getContentType());
        try {
            PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                    bucketName, fileKey, file.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            throw new S3AccessException();
        }

        String s3url = amazonS3.getUrl(bucketName, fileKey).toString();

        // FileMetaData record를 생성하여 반환
        return new FileMetaDataDto(
                bucketName,
                originName,
                s3url,
                fileKey,
                file.getSize(),
                LocalDateTime.now(),
                file.getContentType()
        );
    }

    // 새로운 파일 삭제 메서드
    public void deleteFileFromS3(String fileKey) throws S3AccessException {
        String bucketName = s3Config.getBucketName();

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
        } catch (AmazonServiceException e) {
            // S3에서 발생한 오류를 처리
            throw new S3AccessException();
        } catch (SdkClientException e) {
            // 클라이언트 측에서 발생한 오류를 처리
            throw new S3AccessException();
        }
    }

}
