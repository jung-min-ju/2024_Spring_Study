package com.example.sql_processing_ability.post;

import com.example.sql_processing_ability.user.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "post")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    private UserModel userModel;

    @Column(name = "createdAt")
    private Date date;

    @Column(name = "views")
    private Integer views;

    @Column(name = "like_count")
    private Integer like_count;
}
