package spring.study.week3.domain.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.datafaker.annotations.FakeForSchema;
import spring.study.week3.domain.user.model.User;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)  // 사용자 삭제 시 연관된 포스트도 삭제
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}