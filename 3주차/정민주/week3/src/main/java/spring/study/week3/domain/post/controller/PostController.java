package spring.study.week3.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.common.exception.collections.BindingErrors;
import spring.study.week3.domain.facade.FacadeService;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.post.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PostController {
    private final FacadeService facadeService;
    private final PostService postService;

    @PostMapping("/testing/testingPost")
    public ResponseEntity<?> createTestingPost (@RequestBody @Valid SeedDto seedDto, BindingResult bindingResult) throws IOException {
        handleBindingErrors(bindingResult);
        System.out.println("컨트롤러");
        List<Post> postList = facadeService.createTestingPost(seedDto);
        return ResponseEntity.ok(postList );
    }

    @GetMapping("/post/list")
    public ResponseEntity<List<Post>> getPosts( @RequestParam("type") String type, @RequestParam("count") int count) {
        List<Post> postList = postService.getPosts(type, count);
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/post/search")
    public ResponseEntity<List<Post>> searchPosts( @RequestParam("type") String type, @RequestParam("text") String text) {
        List<Post> searchedPosts = postService.searchPosts(type, text);
        return ResponseEntity.ok( searchedPosts );
    }


    public void handleBindingErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BindingErrors();
        }
    }

}
