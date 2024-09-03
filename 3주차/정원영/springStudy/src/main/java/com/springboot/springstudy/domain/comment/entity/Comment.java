package com.springboot.springstudy.domain.comment.entity;


import com.springboot.springstudy.domain.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String content;

    @NotNull
    private String author;

    @NotNull
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, String author, LocalDateTime date) {
        this.content = content;
        this.author = author;
        this.date = date;
    }
}
