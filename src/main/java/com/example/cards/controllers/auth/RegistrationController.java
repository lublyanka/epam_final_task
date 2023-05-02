package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {


    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        if (registeredUser == null) {
            return EMAIL_ALREADY_EXISTS;
        }
        return ResponseEntity.ok(registeredUser);
    }
}
