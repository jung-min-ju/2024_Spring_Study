package com.springboot.springstudy.common.testing.service;

import com.github.javafaker.Faker;
import com.springboot.springstudy.common.testing.dto.UserTestReq;
import com.springboot.springstudy.domain.user.entity.User;
import com.springboot.springstudy.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestUserService {

    private final UserRepository userRepository;

    @Transactional
    public List<User> randomCreateUser(UserTestReq userTestReq){

        Random rSeed = new Random(userTestReq.seed());
        Faker faker = new Faker(rSeed);

        List<User> userList = new ArrayList<>();

        for(int i=0; i<100; i++){
            String testEmail = faker.internet().emailAddress();
            String testPassword = faker.internet().password();

            User user = User.builder()
                    .email(testEmail)
                    .password(testPassword)
                    .build();

            userList.add(user);
        }

        userRepository.saveAll(userList);

        return userList;
    }
}
