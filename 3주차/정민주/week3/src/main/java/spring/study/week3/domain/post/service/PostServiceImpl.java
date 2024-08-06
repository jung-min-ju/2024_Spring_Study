package spring.study.week3.domain.post.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.common.util.UtilService;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.post.repository.PostRepository;
import spring.study.week3.domain.user.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UtilService utilService;

    @Override
    public List<Post> createTestingPost(SeedDto seedDto, List<User> userList) {

        Faker faker = utilService.getFaker(seedDto);
        Random random = new Random();

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            // Faker를 사용하여 더미 데이터 생성
            String title = faker.lorem().sentence(); // 제목
            String content = faker.lorem().paragraph(); // 내용
            int views = faker.number().numberBetween(1,100000); // 조회수
            int likeCount = faker.number().positive(); // 조회수
            Date date = Date.from(faker.timeAndDate().past()); // 날짜

            // 사용자 목록에서 랜덤하게 사용자 선택
            User randomUser = getRandomUser(userList, random);

            // Post 객체 생성
            Post post = Post.builder()
                    .title(title)
                    .content(content)
                    .views(views)
                    .likeCount(likeCount)
                    .date(date)
                    .user(randomUser)
                    .build();

            // 리스트에 추가
            posts.add(post);
        }

        postRepository.saveAll(posts);

        return posts;
    }

    @Override
    public List<Post> getPosts(String type, int count) {
        return postRepository.findTopPostsByTypeAndCount(type, count);
    }

    @Override
    public List<Post> searchPosts(String type, String text) {
        return postRepository.searchPostsByTypeAndText(type, text);
    }

    // UserList에서 랜덤한 User를 선택하는 메서드
    private User getRandomUser(List<User> userList, Random random) {
        if (userList.isEmpty()) {
            throw new IllegalArgumentException("User list is empty.");
        }
        return userList.get(random.nextInt(userList.size())); // 랜덤하게 User 선택
    }


}
