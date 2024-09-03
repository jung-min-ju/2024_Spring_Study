package com.example.filemanage.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);
}
