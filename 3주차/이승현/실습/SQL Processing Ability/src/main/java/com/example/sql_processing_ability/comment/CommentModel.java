package com.example.sql_processing_ability.comment;

import com.example.sql_processing_ability.post.PostModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @ManyToOne
    private PostModel post;

    @Column(name = "createdAt")
    private LocalDate date;
}
