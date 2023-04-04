package com.example.cards;

import com.example.cards.repositories.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginRequest {

    @Autowired
    private UserRepository userRepository;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
