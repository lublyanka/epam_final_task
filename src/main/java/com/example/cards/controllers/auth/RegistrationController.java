package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;
import static com.example.cards.enums.Responses.INVALID_CAPTCHA;

import com.example.cards.entities.User;
import com.example.cards.services.CaptchaService;
import com.example.cards.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  @SneakyThrows
  @PostMapping("/registration")
  public ResponseEntity<?> registerUser(@RequestBody User user, @RequestHeader String gRecaptchaResponse) {

    if(!CaptchaService.verify(gRecaptchaResponse)){
        return INVALID_CAPTCHA;
    }
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
