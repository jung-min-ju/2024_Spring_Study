package com.example.filemanage.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String username;

    private String password;

    @Builder
    public Users(String email, String PhoneNumber, String username, String password) {
        this.email = email;
        this.phoneNumber = PhoneNumber;
        this.username = username;
        this.password = password;
    }
}
