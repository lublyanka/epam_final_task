package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.INVALID_EMAIL_OR_PASSWORD;

import com.example.cards.requests.LoginRequest;
import com.example.cards.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {


  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    String token = authenticationService.checkUserPassword(password, email);
    if (token == null) return INVALID_EMAIL_OR_PASSWORD;

    return ResponseEntity.ok(token);
  }
}
