package com.example.sql_processing_ability.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostResponseDTO {

    private Integer postId;
    private String title;
    private String content;
    private Integer userId;
    private Date date;
    private Integer views;
    private Integer like_count;

    public PostResponseDTO(PostModel postModel){
        this.postId = postModel.getPostId();
        this.title = postModel.getTitle();
        this.content = postModel.getContent();
        this.userId = postModel.getUserModel().getUserId();
        this.date = postModel.getDate();
        this.views = postModel.getViews();
        this.like_count = postModel.getLike_count();
    }

}
