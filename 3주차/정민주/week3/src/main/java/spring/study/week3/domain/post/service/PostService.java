package spring.study.week3.domain.post.service;

import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.user.model.User;

import java.util.List;

public interface PostService {
    List<Post> createTestingPost(SeedDto seedDto, List<User> userList);
    List<Post> getPosts(String type, int count);
    List<Post> searchPosts(String type, String text);
}
