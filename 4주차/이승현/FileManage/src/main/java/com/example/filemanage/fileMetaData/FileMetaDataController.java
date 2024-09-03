package com.example.filemanage.fileMetaData;

import com.example.filemanage.common.SuccessRes;
import com.example.filemanage.jwt.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileMetaDataController {
    private final FileMetaDataService fileMetaDataService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestParam("image") MultipartFile imageFiles) throws IOException {

        fileMetaDataService.uploadFile(userDetails.getId(), imageFiles);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessRes.from("파일 업로드 성공."));
    }

    @GetMapping()
    public ResponseEntity<?> getFiles(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                      @RequestParam(value = "page") Integer page) {

        return ResponseEntity.status(HttpStatus.OK).body(fileMetaDataService.getMyFiles(userDetails.getId(), page));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                          @PathVariable(value = "id") Integer fileId) throws IOException {

        return fileMetaDataService.downloadFile(userDetails.getId(), fileId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                        @PathVariable(value = "id") Integer fileId) {
        fileMetaDataService.deleteFile(userDetails.getId(), fileId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
