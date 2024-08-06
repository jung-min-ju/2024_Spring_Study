package spring.study.week3.domain.post.repository;

import spring.study.week3.domain.post.model.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findTopPostsByTypeAndCount(String type, int count);
    List<Post> searchPostsByTypeAndText(String type, String text);
}
