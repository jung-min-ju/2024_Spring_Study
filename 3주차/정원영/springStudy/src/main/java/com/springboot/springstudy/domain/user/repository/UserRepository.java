package com.springboot.springstudy.domain.user.repository;

import com.springboot.springstudy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT e FROM User e WHERE e.id BETWEEN :startId AND :endId")
    List<User> findWithIdRange(@Param("startId") Integer startId, @Param("endId") Integer endId);
}

