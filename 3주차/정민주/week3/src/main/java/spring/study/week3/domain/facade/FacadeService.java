package spring.study.week3.domain.facade;

import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.user.dto.DummyUserRspDto;

import java.util.List;

public interface FacadeService {
    List<DummyUserRspDto> createTestingUser(SeedDto seedDto);
    List<Post> createTestingPost(SeedDto seedDto);
}
