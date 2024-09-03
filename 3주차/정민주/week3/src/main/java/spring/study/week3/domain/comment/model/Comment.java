package spring.study.week3.domain.comment.model;

import jakarta.persistence.*;
import lombok.*;
import spring.study.week3.domain.post.model.Post;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}