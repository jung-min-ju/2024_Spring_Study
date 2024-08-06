package com.example.sql_processing_ability.testing;

import com.example.sql_processing_ability.post.PostResponseDTO;
import com.example.sql_processing_ability.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/testing")
public class TestingController {
    private final TestingService testingService;

    @PostMapping("/testingUser")
    public ResponseEntity<List<UserResponseDTO>> testingUser(@RequestBody SeedDTO seedDTO){
        List<UserResponseDTO> users =  testingService.createUsers(seedDTO)
                .stream()
                .map(UserResponseDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @PostMapping("/testingPost")
    public ResponseEntity<List<PostResponseDTO>> testingPost(@RequestBody SeedDTO seedDTO){
        List<PostResponseDTO> posts = testingService.createPost(seedDTO)
                .stream()
                .map(PostResponseDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(posts);
    }
}
