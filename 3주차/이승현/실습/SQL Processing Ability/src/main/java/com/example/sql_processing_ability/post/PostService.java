package com.example.sql_processing_ability.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Page<PostModel> getPosts(String type, Integer number) {
        Pageable pageable;
        Page<PostModel> posts;
        switch (type) {
            case "most":
                pageable = PageRequest.of(0, number, Sort.by(Sort.Direction.ASC, "view"));
                posts = postRepository.findMostViewPost(pageable);
                break;
            case "recent":
                pageable = PageRequest.of(0, number, Sort.by(Sort.Direction.ASC, "date"));
                posts = postRepository.findRecentPost(pageable);
                break;
            default:
                posts = null;
                break;
        }
        return posts;
    }

    public List<PostModel> getPost(String type, String text) {
        List<PostModel> posts;
        switch (type) {
            case "title":
                posts = postRepository.findByTitleContaining(text);
                break;
            case "content":
                posts = postRepository.findByContentContaining(text);
                break;
            case "writer":
                posts = postRepository.findByUserEmail(text);
                break;
            default:
                posts = null;
                break;
        }
        return posts;
    }
}
