package com.example.cards.controllers.auth;

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
        if (userService.isExistsByEmail(user)) {
            return ResponseEntity.status(409).body("Email already exists");
        }
        user = userService.updateUser(user);
        return ResponseEntity.ok(user);

    }


}
