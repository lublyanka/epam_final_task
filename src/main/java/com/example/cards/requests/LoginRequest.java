package com.example.cards.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

/** The type Login request. */
@RequiredArgsConstructor
public class LoginRequest {

  @NotBlank private final String email;

  @NotBlank private final String password;

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }
}
