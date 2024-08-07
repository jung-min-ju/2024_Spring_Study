package com.springboot.springstudy.common.testing.service;

import com.github.javafaker.Faker;
import com.springboot.springstudy.common.testing.dto.PostTestReq;
import com.springboot.springstudy.domain.post.entity.Post;
import com.springboot.springstudy.domain.post.repository.PostRepository;
import com.springboot.springstudy.domain.user.entity.User;
import com.springboot.springstudy.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TestPostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Transactional
    public List<Post> randomCreatePost(PostTestReq postTestReq){

        Random rSeed = new Random(postTestReq.seed());
        Faker faker = new Faker(rSeed);

        List<Post> postList = new ArrayList<>();
        List<User> userList = userRepository.findWithIdRange(1, 100);

        for(int i=0; i<100; i++){
            Post testPost = Post.builder()
                    .title(faker.lorem().sentence())
                    .content(faker.lorem().paragraph())
                    .date(faker.date().past(10, TimeUnit.DAYS))
                    .views(faker.number().numberBetween(10, 100))
                    .like_count(faker.number().numberBetween(10, 100))
                    .user(userList.get(i))
                    .build();

            postList.add(testPost);
        }

        postRepository.saveAll(postList);

        return postList;
    }
}
