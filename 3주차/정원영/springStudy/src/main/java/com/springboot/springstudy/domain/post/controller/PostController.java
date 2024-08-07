package com.springboot.springstudy.domain.post.controller;

import com.springboot.springstudy.domain.post.entity.Post;
import com.springboot.springstudy.domain.post.service.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity<?> getListByOrder(@RequestParam("type") String type,
                                     @RequestParam("number") @Min(1) Integer number){

        List<Post> postList = postService.getPostList(type, number);


        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getListByKeyword(@RequestParam("type") String type,
                                              @RequestParam("text") String text){

        List<Post> postList = postService.searchPosts(type, text);

        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }
}
