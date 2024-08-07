package com.springboot.springstudy.common.testing.controller;


import com.springboot.springstudy.common.testing.dto.PostTestReq;
import com.springboot.springstudy.common.testing.service.TestPostService;
import com.springboot.springstudy.common.testing.service.TestUserService;
import com.springboot.springstudy.domain.post.entity.Post;
import com.springboot.springstudy.common.testing.dto.UserTestReq;
import com.springboot.springstudy.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/testing/")
public class TestController {

    private final TestUserService testUserService;

    private final TestPostService testPostService;

    @PostMapping("/testingUser")
    public ResponseEntity<?> testUser(@Valid @RequestBody UserTestReq userTestReq){

        List<User> userList = testUserService.randomCreateUser(userTestReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(userList);
    }

    @PostMapping("/testingPost")
    public ResponseEntity<?> testPost(@Valid @RequestBody PostTestReq postTestReq){

        List<Post> postList = testPostService.randomCreatePost(postTestReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(postList);
    }
}
