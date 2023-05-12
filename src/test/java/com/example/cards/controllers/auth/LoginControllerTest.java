package com.example.cards.controllers.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.cards.requests.LoginRequest;
import com.example.cards.services.AuthenticationService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LoginControllerTest {
  private final AuthenticationService authenticationService = mock(AuthenticationService.class);
  private final LoginController loginController = new LoginController(authenticationService);

  @Test
  public void testAuthenticateUser_ValidCredentials_ReturnsToken() {
    LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

    String token = "mockedToken";
    when(authenticationService.isRequestEmpty(anyString(), anyString())).thenReturn(false);
    when(authenticationService.isUserValid(anyString(), anyString()))
        .thenReturn(Optional.of(token));

    ResponseEntity<?> response = loginController.authenticateUser(loginRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(token, response.getBody());
  }

  @Test
  public void testAuthenticateUser_RequiredFieldsEmpty_ReturnsBadRequest() {
    LoginRequest loginRequest = new LoginRequest(null, null);

    when(authenticationService.isRequestEmpty(anyString(), anyString())).thenReturn(true);

    ResponseEntity<?> response = loginController.authenticateUser(loginRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testAuthenticateUser_InvalidCredentials_ReturnsUnauthorized() {
    LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

    when(authenticationService.isRequestEmpty(anyString(), anyString())).thenReturn(false);
    when(authenticationService.isUserValid(anyString(), anyString())).thenReturn(Optional.empty());

    ResponseEntity<?> response = loginController.authenticateUser(loginRequest);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void testAuthenticateUser_UserBlocked_ReturnsForbidden() {
    LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

    when(authenticationService.isRequestEmpty(anyString(), anyString())).thenReturn(false);
    when(authenticationService.isUserValid(anyString(), anyString()))
        .thenReturn(Optional.of("false"));

    ResponseEntity<?> response = loginController.authenticateUser(loginRequest);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
