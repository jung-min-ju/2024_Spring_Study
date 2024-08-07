package com.springboot.springstudy.domain.post.repository;

import com.springboot.springstudy.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findTopByOrderByViewsDesc(Pageable pageable);

    List<Post> findTopByOrderByDateDesc(Pageable pageable);

    List<Post> findAllByTitleContaining(String title);

    List<Post> findAllByContentContaining(String content);

    List<Post> findAllByUserEmail(String email);
}
