package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.cards.entities.User;
import com.example.cards.utils.JwtTokenUtil;
import com.example.cards.utils.UserPrincipal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AuthenticationServiceTest {
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
  private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
  private final UserService userService = mock(UserService.class);
  private final AuthenticationService authenticationService =
      new AuthenticationService(passwordEncoder, userService, jwtTokenUtil);



  @Test
  void isUserValid() {
    String password = "password";
    String email = "user@example.com";
    User user = new User();
    user.setEmail(email);
    String encodedPassword = passwordEncoder.encode(password);
    user.setPassword(encodedPassword);
    user.setBlocked(false);
    Optional<?> result;

    // Test when user does not exist
    when(userService.getUserByEmail(email)).thenReturn(null);
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isEmpty());

    // Test when user is blocked
    user.setBlocked(true);
    when(userService.getUserByEmail(email)).thenReturn(user);
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isPresent());
    assertEquals("false", result.get());

    // Test when password is invalid
    when(userService.getUserByEmail(email)).thenReturn(user);
    user.setPassword("Pass1");
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isEmpty());

    // Test when user exists and password is valid
    user.setPassword(encodedPassword);
    user.setBlocked(false);
    when(userService.getUserByEmail(email)).thenReturn(user);
    UserPrincipal up = new UserPrincipal(user);
    when(jwtTokenUtil.generateJwtToken(any())).thenReturn("token");
    // when(userService.updateUserLastLogin(user)).then(user.setLastLogin(Timestamp.from(Instant.now()));
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isPresent());
    assertEquals(jwtTokenUtil.generateJwtToken(up), result.get());
  }

  @Test
  void isRequestEmpty() {
    assertFalse(authenticationService.isRequestEmpty("user@example.com", "password"));
    assertTrue(authenticationService.isRequestEmpty("", "password"));
    assertTrue(authenticationService.isRequestEmpty("user@example.com", ""));
    assertTrue(authenticationService.isRequestEmpty("", ""));
  }
}
