package com.example.cards.controllers.auth;

import com.example.cards.requests.LoginRequest;
import com.example.cards.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.cards.enums.Responses.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {


  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    if (authenticationService.isRequestEmpty(email, password))
        return REQUIRED_FIELDS_ARE_EMPTY;

    Optional<?> user = authenticationService.isUserValid(password, email);
    if (user.isEmpty()) return INVALID_EMAIL_OR_PASSWORD;
    if (user.get() instanceof Boolean) return USER_IS_BLOCKED;
    String token = user.get().toString();
    if (token == null) return INVALID_EMAIL_OR_PASSWORD;

    return ResponseEntity.ok(token);
  }


}
