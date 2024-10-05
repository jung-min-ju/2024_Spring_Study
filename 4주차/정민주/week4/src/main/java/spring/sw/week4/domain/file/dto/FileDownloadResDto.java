package spring.sw.week4.domain.file.dto;

import org.springframework.core.io.ByteArrayResource;

public record FileDownloadResDto(
        String fileName,
        String mimeType,
        ByteArrayResource resource
) {}
