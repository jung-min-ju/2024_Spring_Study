package spring.study.week3.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import spring.study.week3.domain.user.dto.DummyUserRspDto;
import spring.study.week3.domain.user.model.QUser;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DummyUserRspDto> getUserSummary() {
        QUser user = QUser.user; // Reference to the QUser instance

        //Projections.constructor는 DummyUserRspDto의 생성자를 사용하여 쿼리 결과를 직접 매핑합니다
        return jpaQueryFactory
                .select(Projections.constructor(
                        DummyUserRspDto.class,
                        user.email,
                        user.password
                ))
                .from(user) // User 엔티티를 쿼리
                .fetch(); // 쿼리를 실행하고 결과를 반환
    }
}
