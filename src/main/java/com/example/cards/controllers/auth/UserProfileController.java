package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;
import static com.example.cards.enums.Responses.USER_NOT_FOUND;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/** The User profile controller. */
@RestController
@RequestMapping("/api/auth/profile")
public class UserProfileController {

  @Autowired private UserService userService;

  /**
   * Load user profile response entity.
   *
   * @param token the JWT token
   * @return the response entity
   */
  @GetMapping("")
  public ResponseEntity<?> loadUserProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUserByToken(token);
    return ResponseEntity.ok(user);
  }

  /**
   * Update user response entity.
   *
   * @param userToUpdate the updating user
   * @return the response entity
   */
  @PostMapping("/update")
  public ResponseEntity<?> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody User userToUpdate) {
    User userCurrent = userService.getUserByToken(token);
    if (userCurrent == null) return USER_NOT_FOUND;

    User userToSave = userService.getUserById(userToUpdate.getId()).orElse(null);
    if (userToSave == null) return USER_NOT_FOUND;

    if(!userCurrent.getId().equals(userToUpdate.getId()) && !(userCurrent.getRole().equals("ADMIN")))
      return ResponseEntity.badRequest().body("You can't update other users");

    User userByEmail = userService.getUserByEmail(userToUpdate.getEmail());

    if (userByEmail != null && ! (userToSave.getId().equals(userByEmail.getId()))) {
      return EMAIL_ALREADY_EXISTS;
    }
    userToSave = userService.updateUser(userToUpdate, userToSave);
    return ResponseEntity.ok(userToSave);
  }
}
