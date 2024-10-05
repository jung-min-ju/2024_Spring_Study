package spring.sw.week4.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.sw.week4.common.config.spring.S3Config;
import spring.sw.week4.common.exception.collections.NotFound.NotFoundFileException;
import spring.sw.week4.common.exception.collections.business.database.DuplicateUniqueKeyException;
import spring.sw.week4.common.exception.collections.io.S3AccessException;
import spring.sw.week4.common.exception.collections.io.NotMatchUserToFileOwner;
import spring.sw.week4.common.s3.S3Service;
import spring.sw.week4.domain.file.dto.FileMetaDataDto;
import spring.sw.week4.domain.file.dto.GetFileResDto;
import spring.sw.week4.domain.file.repository.FileRepository;
import spring.sw.week4.domain.file.dto.FileDownloadResDto;
import spring.sw.week4.domain.file.model.FileMetaInfo;
import spring.sw.week4.domain.user.model.User;
import spring.sw.week4.domain.user.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final S3Service s3Service;
    private final UserService userService;
    private final AmazonS3 amazonS3;  // AmazonS3 클라이언트
    private final S3Config s3Config;

    @Override
    public void uploadFile(MultipartFile multipartFile) throws S3AccessException {
        FileMetaDataDto fileDataDto = s3Service.uploadImageToS3(multipartFile);
        User user = userService.findNowUser();

        FileMetaInfo fileMetaInfo = FileMetaInfo.builder()
                .fileName(fileDataDto.fileName())
                .filePath(fileDataDto.filePath())
                .fileKey(fileDataDto.fileKey())
                .fileSize(fileDataDto.fileSize())
                .uploadTime(fileDataDto.uploadTime())
                .contentType(fileDataDto.contentType())
                .user(user)
                .build();

        try{
            fileRepository.save(fileMetaInfo);
        } catch (DataIntegrityViolationException e) {
            //만약 실패 했다면, s3에 방금 올린 파일 수동 삭제 (s3는 트랜젝션 영향 안받음)
            s3Service.deleteFileFromS3(fileMetaInfo.getFileKey());
            throw new DuplicateUniqueKeyException();
        }

        //양방향 매핑
        user.addFile(fileMetaInfo);

    }

    @Override
    public List<GetFileResDto> getFiles() {
        User user = userService.findNowUser();

        return user.getFiles().stream()
                .map(file -> new GetFileResDto(file.getId(), file.getFilePath()))
                .collect(Collectors.toList());
    }

    @Override
    public FileDownloadResDto downloadFile(Integer id) throws IOException {
        User currentUser = userService.findNowUser();

        // 파일 메타 정보 조회
        FileMetaInfo fileMetaInfo = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundFileException());

        // 현재 사용자와 파일 소유자 확인
        if (!fileMetaInfo.getUser().getId().equals(currentUser.getId())) {
            throw new NotMatchUserToFileOwner();
        }
        // S3에서 파일 다운 로드
        S3Object s3Object = amazonS3.getObject(s3Config.getBucketName(), fileMetaInfo.getFileKey());
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        // InputStreamResource로 변환
        // 스트림을 메모리에 로드하여 여러 번 읽기 가능하도록 함
        byte[] bytes = IOUtils.toByteArray(inputStream);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return new FileDownloadResDto(fileMetaInfo.getFileName(), fileMetaInfo.getContentType(), resource);
    }

    @Override
    public void deleteFile(Integer id) throws S3AccessException {
        // 파일 메타 정보 조회
        FileMetaInfo fileMetaInfo = fileRepository.findById(id)
                .orElseThrow(() -> new NotFoundFileException());

        s3Service.deleteFileFromS3(fileMetaInfo.getFileKey());

        // user 에서 파일 삭제 -> 파일 자동 삭제
        User user = fileMetaInfo.getUser();
        user.getFiles().remove(fileMetaInfo);

    }

}
