package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.*;

import com.example.cards.requests.LoginRequest;
import com.example.cards.services.AuthenticationService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The Login controller. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

  private final AuthenticationService authenticationService;

  /**
   * Authenticate user response entity.
   *
   * @param loginRequest the login request with email and password
   * @return the response entity
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    if (loginRequest == null|| loginRequest.getEmail() == null || loginRequest.getPassword() == null)
      return REQUIRED_FIELDS_ARE_EMPTY;
    String email = loginRequest.getEmail().trim().toLowerCase();
    String password = loginRequest.getPassword().trim();

    if (authenticationService.isRequestEmpty(email, password)) return REQUIRED_FIELDS_ARE_EMPTY;

    Optional<?> user = authenticationService.isUserValid(password, email);
    if (user.isEmpty()) return INVALID_EMAIL_OR_PASSWORD;
    if (user.get().equals("false")) return USER_IS_BLOCKED;
    String token = user.get().toString();
    if (token == null) return INVALID_EMAIL_OR_PASSWORD;

    return ResponseEntity.ok(token);
  }
}