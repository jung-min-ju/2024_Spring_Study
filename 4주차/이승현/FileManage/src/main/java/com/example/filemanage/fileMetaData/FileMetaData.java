package com.example.filemanage.fileMetaData;

import com.example.filemanage.user.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FileMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String file_name;

    private String file_path;

    private Long file_size;

    private LocalDateTime upload_time;

    private String content_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public FileMetaData(String file_name, String file_path, Long file_size, LocalDateTime upload_time, String content_type, Users user) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_size = file_size;
        this.upload_time = upload_time;
        this.content_type = content_type;
        this.user = user;
    }
}
