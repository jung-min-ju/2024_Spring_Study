package spring.study.week3.domain.user.repository;

import spring.study.week3.domain.user.dto.DummyUserRspDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<DummyUserRspDto> getUserSummary();
}
