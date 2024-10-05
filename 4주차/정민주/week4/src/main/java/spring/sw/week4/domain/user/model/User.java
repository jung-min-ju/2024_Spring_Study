package spring.sw.week4.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import spring.sw.week4.domain.file.model.FileMetaInfo;
import spring.sw.week4.domain.user.enums.Role;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<FileMetaInfo> files;

    @Builder
    public User(String name, String email, String password, String phoneNum, Role role) {
        this.name = name;
        this.email=email;
        this.phoneNum=phoneNum;
        this.password=password;
        this.role = role;
        this.files = new HashSet<>();
    }

    public void addFile(FileMetaInfo fileMetaInfo) {
        files.add(fileMetaInfo);
    }

    public void deleteFile(FileMetaInfo fileMetaInfo){
        files.remove(fileMetaInfo);
    }
}
