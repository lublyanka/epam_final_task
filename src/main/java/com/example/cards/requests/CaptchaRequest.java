package com.example.cards.requests;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/** The Captcha request. */
@RequiredArgsConstructor
public class CaptchaRequest {

  @NotBlank
  @Getter @Setter private boolean success;

  @Getter @Setter
  private Timestamp
      challenge_ts; // timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)

  @Getter @Setter private String hostname; // the hostname of the site where the reCAPTCHA was solved
  @Getter @Setter private String[] error_codes; // optional error-codes
}
