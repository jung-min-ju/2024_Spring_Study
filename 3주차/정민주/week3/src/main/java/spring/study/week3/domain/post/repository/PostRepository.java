package spring.study.week3.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.study.week3.domain.post.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
}
