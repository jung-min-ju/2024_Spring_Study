package spring.study.week3.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.study.week3.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
