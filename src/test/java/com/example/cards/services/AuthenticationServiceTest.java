package com.example.cards.services;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import com.example.cards.utils.JwtTokenUtil;
import com.example.cards.utils.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
  private final UserRepository userRepository = mock(UserRepository.class);
  private final JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
  private final UserService userService =
          new UserService(passwordEncoder, userRepository, jwtTokenUtil);
  private final AuthenticationService authenticationService =
      new AuthenticationService(passwordEncoder, userService, jwtTokenUtil);

  @Test
  void isUserValid() {
    String password = "password";
    String email = "user@example.com";
    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setBlocked(false);

    // Mock the userService's getUserByEmail method
    when(userService.getUserByEmail(email)).thenReturn(user);

    // Test when user exists and password is valid
    Optional<?> result = authenticationService.isUserValid(password, email);
    assertTrue(result.isPresent());
    assertEquals(jwtTokenUtil.generateJwtToken(new UserPrincipal(user)), result.get());

    // Test when user does not exist
    when(userService.getUserByEmail(email)).thenReturn(null);
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isEmpty());

    // Test when password is invalid
    when(userService.getUserByEmail(email)).thenReturn(user);
    //when(authenticationService.isPasswordValid(password, user)).thenReturn(false);
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isEmpty());

    // Test when user is blocked
    user.setBlocked(true);
    when(userService.getUserByEmail(email)).thenReturn(user);
    result = authenticationService.isUserValid(password, email);
    assertTrue(result.isPresent());
    assertEquals(true, result.get());

  }

  @Test
  void isRequestEmpty() {
    assertFalse(authenticationService.isRequestEmpty("user@example.com", "password"));
    assertTrue(authenticationService.isRequestEmpty("", "password"));
    assertTrue(authenticationService.isRequestEmpty("user@example.com", ""));
    assertTrue(authenticationService.isRequestEmpty("", ""));
  }
}
