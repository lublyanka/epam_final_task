package com.example.cards.controllers.auth;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;
import static com.example.cards.enums.Responses.USER_NOT_FOUND;

@RestController
@RequestMapping("/api/auth/profile")
public class UserProfileController {


  @Autowired private UserService userService;

  @GetMapping("")
  public ResponseEntity<?> loadUserProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUserByTokenWithCountry(token);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/update")
  public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
    User userToSave = userService.getUserById(updatedUser.getId()).orElse(null);

    if (userToSave == null)
      return USER_NOT_FOUND;

    User userByEmail = userService.getUserByEmail(updatedUser.getEmail());

    if (userByEmail != null && userToSave.getId().equals(userByEmail.getId())) {
      return EMAIL_ALREADY_EXISTS;
    }
    userToSave = userService.updateUser(updatedUser, userToSave);
    return ResponseEntity.ok(userToSave);
  }

  @PutMapping("/{userId}/block")
  public ResponseEntity<?> blockUser(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {

    Optional<User> userOptional = userService.block(userId);

    if (userOptional.isPresent()) {
      return ResponseEntity.ok("User blocked successfully.");
    } else {
      return USER_NOT_FOUND;
    }
  }

  @PutMapping("/{userId}/unblock")
  public ResponseEntity<?> unblockUser(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {

    Optional<User> userOptional = userService.unblock(userId);

    if (userOptional.isPresent()) {
      return ResponseEntity.ok("User unblocked successfully.");
    } else {
      return USER_NOT_FOUND;
    }
  }
}