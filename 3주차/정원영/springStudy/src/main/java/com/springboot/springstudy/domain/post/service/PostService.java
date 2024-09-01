package com.springboot.springstudy.domain.post.service;

import com.springboot.springstudy.domain.post.entity.Post;
import com.springboot.springstudy.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostList(String type, Integer number){

        Pageable pageable = PageRequest.of(0, number);

        if(type.equals("most")){
            return postRepository.findTopByOrderByViewsDesc(pageable);
        }else if(type.equals("recent")){
            return postRepository.findTopByOrderByDateDesc(pageable);
        }else{
            throw new IllegalArgumentException("Invalid type : " + type);
        }

    }

    public List<Post> searchPosts(String type, String text) {

        if (type.equals("title")) {
            return postRepository.findAllByTitleContaining(text);
        } else if (type.equals("content")) {
            return postRepository.findAllByContentContaining(text);
        } else if (type.equals("writer")) {
            return postRepository.findAllByUserEmail(text);
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
