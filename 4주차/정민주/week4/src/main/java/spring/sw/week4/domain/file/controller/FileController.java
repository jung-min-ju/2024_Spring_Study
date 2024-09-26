package spring.sw.week4.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.sw.week4.common.config.constant.ControllerConstants;
import spring.sw.week4.common.exception.collections.io.S3AccessException;
import spring.sw.week4.domain.file.dto.GetFileResDto;
import spring.sw.week4.domain.file.dto.FileDownloadResDto;
import spring.sw.week4.domain.file.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("files")MultipartFile multipartFile) throws S3AccessException {
        fileService.uploadFile(multipartFile);
        return new ResponseEntity<>(ControllerConstants. completeFileUpload, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFilesList() {
        List<GetFileResDto> fileList = fileService.getFiles();
        return ResponseEntity.ok().body(fileList);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws IOException {
        FileDownloadResDto fileDownloadRes = fileService.downloadFile(id);

        // HTTP 응답을 구성 하여 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileDownloadRes.fileName())
                .contentType(MediaType.parseMediaType(fileDownloadRes.mimeType()))
                .contentLength(fileDownloadRes.resource().contentLength())
                .body(fileDownloadRes.resource());
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteFile(@PathVariable Integer id) throws S3AccessException {
        fileService.deleteFile(id);

        return new ResponseEntity<>(ControllerConstants.completeFileDelete, HttpStatus.OK);
    }



}
