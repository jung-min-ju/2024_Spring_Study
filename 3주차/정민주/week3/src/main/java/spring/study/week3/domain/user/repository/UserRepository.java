package spring.study.week3.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.study.week3.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
}
