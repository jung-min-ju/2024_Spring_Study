package spring.study.week3.domain.user.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.stereotype.Service;
import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.common.util.UtilService;
import spring.study.week3.domain.user.dto.DummyUserRspDto;
import spring.study.week3.domain.user.model.User;
import spring.study.week3.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UtilService utilService;

    @Override
    public void createTestingUser(SeedDto seedDto) {
        userRepository.deleteAll();

        Faker faker = utilService.getFaker(seedDto);

        // populate 메서드를 사용하여 100개의 User 객체 생성
        // 100개의 User 객체 생성
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            // Faker를 사용하여 더미 데이터 생성
            String email = faker.internet().emailAddress(); // 이메일
            String password = faker.internet().password(); // 비밀번호
            int createdAt = faker.number().positive(); // 생성 시각 (예: UNIX 타임스탬프)
            int updatedAt = faker.number().positive(); // 업데이트 시각 (예: UNIX 타임스탬프)

            // User 객체 생성
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();

            // 리스트에 추가
            users.add(user);
        }

        userRepository.saveAll(users);

    }

    @Override
    public List<DummyUserRspDto> getUserSummary() {
        return userRepository.getUserSummary();
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


}

///v1/testing/testingUser 에 POST 방식으로 body 에 seed 를 담아서 보낼 시 ,
// 해당 seed에 해당하는 Testing User가 100개 생성되어 DB에 Insert 된다.
