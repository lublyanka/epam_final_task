package com.example.cards.controllers.auth;

import static com.example.cards.enums.Responses.EMAIL_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.cards.entities.User;
import com.example.cards.services.CaptchaService;
import com.example.cards.services.UserService;
import java.util.Optional;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RegistrationControllerTest {
  private final UserService userService = mock(UserService.class);
  private final RegistrationController registrationController =
      new RegistrationController(userService);

  @SneakyThrows
  @Test
  public void testRegisterUser_ValidUser_ReturnsRegisteredUser() {
    try (MockedStatic<CaptchaService> utilities = Mockito.mockStatic(CaptchaService.class)) {
      User user = new User();
      user.setEmail("test@example.com");
      user.setPassword("password");

      String gRecaptchaResponse = "gRecaptchaResponse";
      utilities.when(() -> CaptchaService.verify(gRecaptchaResponse)).thenReturn(Boolean.TRUE);

      when(userService.getValidationUserError(any(User.class))).thenReturn(Optional.empty());
      when(userService.registerUser(any(User.class))).thenReturn(user);

      ResponseEntity<?> response = registrationController.registerUser(user, gRecaptchaResponse);

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(user, response.getBody());
    }
  }

  @SneakyThrows
  @Test
  public void testRegisterUser_ValidationError_ReturnsErrorResponse() {
    try (MockedStatic<CaptchaService> utilities = Mockito.mockStatic(CaptchaService.class)) {
      User user = new User();
      user.setEmail("test@example.com");
      user.setPassword("password");

      String gRecaptchaResponse = "gRecaptchaResponse";
      utilities.when(() -> CaptchaService.verify(gRecaptchaResponse)).thenReturn(Boolean.TRUE);

      ResponseEntity<String> errorResponse = ResponseEntity.badRequest().build();
      when(userService.getValidationUserError(any(User.class)))
          .thenReturn(Optional.of(errorResponse));

      ResponseEntity<?> response = registrationController.registerUser(user, gRecaptchaResponse);

      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertEquals(errorResponse, response);
    }
  }

  @SneakyThrows
  @Test
  public void testRegisterUser_ValidUser_AlreadyExists() {
    try (MockedStatic<CaptchaService> utilities = Mockito.mockStatic(CaptchaService.class)) {
      User user = new User();
      user.setEmail("test@example.com");
      user.setPassword("password");

      String gRecaptchaResponse = "gRecaptchaResponse";
      utilities.when(() -> CaptchaService.verify(gRecaptchaResponse)).thenReturn(Boolean.TRUE);

      when(userService.getValidationUserError(any(User.class))).thenReturn(Optional.empty());
      when(userService.registerUser(any(User.class))).thenReturn(null);

      ResponseEntity<?> response = registrationController.registerUser(user, gRecaptchaResponse);

      assertEquals(EMAIL_ALREADY_EXISTS.getStatusCode(), response.getStatusCode());
      assertEquals(EMAIL_ALREADY_EXISTS.getBody(), response.getBody());
    }
  }

  @SneakyThrows
  @Test
  public void testRegisterUser_InvalidCaptcha_ReturnsErrorResponse() {
    try (MockedStatic<CaptchaService> utilities = Mockito.mockStatic(CaptchaService.class)) {
      User user = new User();
      user.setEmail("test@example.com");
      user.setPassword("password");

      String gRecaptchaResponse = "gRecaptchaResponse";
      utilities.when(() -> CaptchaService.verify(gRecaptchaResponse)).thenReturn(Boolean.FALSE);

      ResponseEntity<?> response = registrationController.registerUser(user, gRecaptchaResponse);

      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertEquals("Invalid captcha", response.getBody());
    }
  }
}
