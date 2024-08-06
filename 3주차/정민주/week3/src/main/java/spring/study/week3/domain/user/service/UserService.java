package spring.study.week3.domain.user.service;

import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.domain.user.dto.DummyUserRspDto;
import spring.study.week3.domain.user.model.User;

import java.util.List;

public interface UserService {
    void createTestingUser(SeedDto seedDto);
    List<DummyUserRspDto> getUserSummary();
    List<User> getAllUser();
}
