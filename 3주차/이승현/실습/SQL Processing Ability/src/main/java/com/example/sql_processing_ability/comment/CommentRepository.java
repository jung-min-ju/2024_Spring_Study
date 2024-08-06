package com.example.sql_processing_ability.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentModel,Integer> {
}
