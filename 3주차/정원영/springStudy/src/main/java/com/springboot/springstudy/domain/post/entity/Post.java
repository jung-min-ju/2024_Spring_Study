package com.springboot.springstudy.domain.post.entity;


import com.springboot.springstudy.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;


@Getter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Date date;

    @NotNull
    private Integer views;

    @NotNull
    private Integer like_count;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content, Date date, Integer views, Integer like_count, User user) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.views = views;
        this.like_count = like_count;
        this.user = user;
    }
}
