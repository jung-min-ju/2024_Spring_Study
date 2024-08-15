package spring.study.week3.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import spring.study.week3.domain.post.model.Post;
import spring.study.week3.domain.post.model.QPost;
import spring.study.week3.domain.user.model.QUser;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findTopPostsByTypeAndCount(String type, int count) {
        QPost post = QPost.post;

        // 조회수 기준으로 정렬
        if ("most".equalsIgnoreCase(type)) {
            return queryFactory.selectFrom(post)
                    .orderBy(post.views.desc())
                    .limit(count)
                    .fetch();
        }

        // 최근 생성일 기준으로 정렬
        if ("recent".equalsIgnoreCase(type)) {
            return queryFactory.selectFrom(post)
                    .orderBy(post.date.desc())
                    .limit(count)
                    .fetch();
        }

        // 잘못된 type 요청 시 빈 리스트 반환
        return List.of();
    }

    @Override
    public List<Post> searchPostsByTypeAndText(String type, String text) {
        QPost post = QPost.post;
        QUser user = QUser.user;  // QUser를 통해 사용자 정보 접근

        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색
        if ("title".equalsIgnoreCase(type)) {
            builder.and(post.title.containsIgnoreCase(text));
        }

        // 내용 검색
        if ("content".equalsIgnoreCase(type)) {
            builder.and(post.content.containsIgnoreCase(text));
        }

        // 작성자 검색
        if ("writer".equalsIgnoreCase(type)) {
            builder.and(post.user.email.containsIgnoreCase(text));  // 이메일로 검색
        }

        // 검색 결과 반환
        return queryFactory.selectFrom(post)
                .leftJoin(post.user, user)
                .where(builder)
                .fetch();
    }
}
