package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The Registration controller. */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

private final UserService userService;

  /**
   * Register user response entity.
   *
   * @param user the user
   * @return the response entity
   */
  @PostMapping("/registration")
  public ResponseEntity<?> registerUser(@RequestBody User user) {

    Optional<?> error = userService.getValidationUserError(user);
    if (error.isPresent() && error.get() instanceof ResponseEntity) {
      return (ResponseEntity<?>) error.get();
    }
    User registeredUser = userService.registerUser(user);

    if (registeredUser == null) {
      return EMAIL_ALREADY_EXISTS;
    }
    return ResponseEntity.ok(registeredUser);
  }
}
