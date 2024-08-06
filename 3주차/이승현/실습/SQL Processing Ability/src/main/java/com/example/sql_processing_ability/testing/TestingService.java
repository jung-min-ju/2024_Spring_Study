package com.example.sql_processing_ability.testing;

import com.example.sql_processing_ability.post.PostModel;
import com.example.sql_processing_ability.post.PostRepository;
import com.example.sql_processing_ability.user.UserModel;
import com.example.sql_processing_ability.user.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TestingService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<UserModel> createUsers(SeedDTO seedDTO) {
        Random random = new Random(seedDTO.getSeed());
        Faker faker = new Faker(random);
        List<UserModel> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            UserModel newUser = new UserModel();
            newUser.setEmail(faker.internet().emailAddress());
            newUser.setPassword(faker.internet().password());
            UserModel savedUser = userRepository.save(newUser);
            users.add(savedUser);
        }
        return users;
    }

    public List<PostModel> createPost(SeedDTO seedDTO){
        Random random = new Random(seedDTO.getSeed());
        Faker faker = new Faker(random);
        List<PostModel> posts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            PostModel newPost = new PostModel();
            newPost.setTitle(faker.lorem().sentence());
            newPost.setContent(faker.lorem().paragraph());
            newPost.setUserModel(userRepository.findById(random.nextInt(100)+1).orElseThrow(IllegalArgumentException::new));
            newPost.setDate(faker.date().past(30, TimeUnit.DAYS));
            newPost.setViews(faker.number().numberBetween(0, 1000));
            newPost.setLike_count(faker.number().numberBetween(0, 500));
            PostModel savePost = postRepository.save(newPost);
            posts.add(savePost);
        }

        return posts;
    }
}
