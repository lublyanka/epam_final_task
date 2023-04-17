package com.example.cards.controllers.auth;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> loadUserProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = userService.getUserByTokenWithCountry(token);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        User userToSave = userService.getUserById(updatedUser.getId()).orElse(null);

        if (userToSave == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

        User userByEmail = userService.getUserByEmail(updatedUser.getEmail());

        if (userByEmail != null
                && !Objects.equals(userToSave.getId(), userByEmail.getId())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        userToSave = userService.updateUser(updatedUser, userToSave);
        return ResponseEntity.ok(userToSave);
    }

    @PutMapping("/{userId}/block")
    public ResponseEntity<?> blockUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            userService.block(userOptional.get());
            return ResponseEntity.ok("User blocked successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/unblock")
    public ResponseEntity<?> unblockUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            userService.unblock(userOptional.get());
            return ResponseEntity.ok("User unblocked successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
