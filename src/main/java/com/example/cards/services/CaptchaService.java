package com.example.cards.services;

import com.example.cards.requests.CaptchaRequest;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * The Captcha service reCAPTCHA v2 <a
 * href="https://developers.google.com/recaptcha/docs/display">...</a>.
 */
@Log4j2
public final class CaptchaService {
  /** The constant url to Google service. */
  public static final String url = "https://www.google.com/recaptcha/api/siteverify";

  private static final String USER_AGENT = "Mozilla/5.0";
  /** The constant googleSecret. */
  public static final String googleSecret = System.getenv("ENV_GSECRET");

  /**
   * Verify a user's response to a reCAPTCHA challenge
   *
   * @param gRecaptchaResponse the response from client request to validate
   * @return boolean
   * @throws IOException the io exception
   */
  public static boolean verify(String gRecaptchaResponse) throws IOException {
    log.info("CaptchaService.verify(): " + url);
    if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
      return false;
    }
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("User-Agent", USER_AGENT);
      headers.add("Accept", "application/json");
      headers.add("Accept-Language", "en-US,en;q=0.5");

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("secret", googleSecret);
      body.add("response", gRecaptchaResponse);

      HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
      ResponseEntity<CaptchaRequest> result =
          restTemplate.postForEntity(url, entity, CaptchaRequest.class);
      log.info("Response code: " + result.getStatusCode().value());
      CaptchaRequest captchaRequest = result.getBody();
      assert captchaRequest != null;
      log.info(captchaRequest.isSuccess());
      return captchaRequest.isSuccess();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
