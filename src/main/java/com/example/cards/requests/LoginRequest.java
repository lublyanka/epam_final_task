package com.example.cards.requests;

import jakarta.validation.constraints.NotBlank;

/** The type Login request. */
public class LoginRequest {

  @NotBlank private String email;

  @NotBlank private String password;

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
