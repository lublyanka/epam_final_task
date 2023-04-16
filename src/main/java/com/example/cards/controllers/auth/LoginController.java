package com.example.cards.controllers.auth;

import com.example.cards.entities.User;
import com.example.cards.requests.LoginRequest;
import com.example.cards.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    public static final ResponseEntity<String> INVALID_EMAIL_OR_PASSWORD = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return INVALID_EMAIL_OR_PASSWORD;
        }

        if (!userService.isPasswordMatch(password, user)) {
            return INVALID_EMAIL_OR_PASSWORD;
        }

        String token = userService.getToken(user);
        return ResponseEntity.ok(token);
    }


}