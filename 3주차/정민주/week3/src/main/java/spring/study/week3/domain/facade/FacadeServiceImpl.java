package spring.study.week3.domain.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.post.service.PostService;
import spring.study.week3.domain.user.dto.DummyUserRspDto;
import spring.study.week3.domain.user.model.User;
import spring.study.week3.domain.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacadeServiceImpl implements FacadeService {
    private final UserService userService;
    private final PostService postService;

    @Override
    public List<DummyUserRspDto> createTestingUser(SeedDto seedDto) {
        //1. user 생성
        userService.createTestingUser(seedDto);
        //2. user 정보 가져 오기
        return userService.getUserSummary();
    }

    @Override
    public List<Post> createTestingPost(SeedDto seedDto) {
        //1. userList 가져오기
        List<User> userList = userService.getAllUser();
        //2. post 생성
        return postService.createTestingPost(seedDto, userList);
    }

}

