package spring.sw.week4.domain.file.model;

import jakarta.persistence.*;
import lombok.*;
import spring.sw.week4.domain.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMetaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @Column(nullable = false, length = 100)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //자동 영속화 예정


    @Builder
    public FileMetaInfo(String fileName, String filePath, String fileKey, Long fileSize, LocalDateTime uploadTime, String contentType, User user) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileKey = fileKey;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
        this.contentType = contentType;
        this.user = user;
    }


}
