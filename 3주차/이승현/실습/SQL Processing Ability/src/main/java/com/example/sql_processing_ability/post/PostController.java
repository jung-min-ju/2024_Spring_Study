package com.example.sql_processing_ability.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public ResponseEntity<PostModel> getPosts(@RequestParam("type") String type, @RequestParam("number") Integer number) {
        Page<PostModel> posts = postService.getPosts(type, number);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> getPost(@RequestParam("type") String type, @RequestParam("text") String text) {
        List<PostResponseDTO> posts = postService.getPost(type, text)
                .stream()
                .map(PostResponseDTO::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
