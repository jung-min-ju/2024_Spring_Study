package com.example.sql_processing_ability.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostModel, Integer> {
    //    @Query(value = "SELECT p FROM PostModel p ORDER BY p.views DESC")
//    List<PostModel> findMostViewPost(Pageable pageable);
    Page<PostModel> findMostViewPost(Pageable pageable);

    //    @Query(value = "SELECT p FROM PostModel p ORDER BY p.date DESC ")
//    List<PostModel> findRecentPost(Pageable pageable);
    Page<PostModel> findRecentPost(Pageable pageable);

    @Query(value = "SELECT p FROM PostModel p WHERE p.title LIKE %:text%")
    List<PostModel> findByTitleContaining(@Param("text") String text);

    @Query(value = "SELECT p FROM PostModel p WHERE p.content LIKE %:text%")
    List<PostModel> findByContentContaining(@Param("text") String text);

    @Query(value = "SELECT p FROM PostModel p WHERE p.userModel.email LIKE :email")
    List<PostModel> findByUserEmail(@Param("email") String email);


}
