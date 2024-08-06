package com.example.sql_processing_ability.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String email;
    private String password;

    public UserResponseDTO(UserModel userModel) {
        this.email = userModel.getEmail();
        this.password = userModel.getPassword();
    }
}
