package com.example.cards.controllers.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.cards.entities.User;
import com.example.cards.services.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RegistrationControllerTest {
  private final UserService userService = mock(UserService.class);
  private final RegistrationController registrationController =
      new RegistrationController(userService);

  @Test
  public void testRegisterUser_ValidUser_ReturnsRegisteredUser() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("password");
    // Set other user properties...

    when(userService.getValidationUserError(any(User.class))).thenReturn(Optional.empty());
    when(userService.registerUser(any(User.class))).thenReturn(user);

    ResponseEntity<?> response = registrationController.registerUser(user);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());
  }

  @Test
  public void testRegisterUser_ValidationError_ReturnsErrorResponse() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("password");
    // Set other user properties...

    ResponseEntity<String> errorResponse = ResponseEntity.badRequest().build();
    when(userService.getValidationUserError(any(User.class)))
        .thenReturn(Optional.of(errorResponse));

    ResponseEntity<?> response = registrationController.registerUser(user);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(errorResponse, response);
  }
}
