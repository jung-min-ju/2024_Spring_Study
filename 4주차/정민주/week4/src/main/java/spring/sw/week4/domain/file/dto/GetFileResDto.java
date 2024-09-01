package spring.sw.week4.domain.file.dto;

public record GetFileResDto(
        Integer fileId,
        String s3Path
) {
}
