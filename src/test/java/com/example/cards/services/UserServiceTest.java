package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final UserRepository userRepository = mock(UserRepository.class);
  private final UserService userService = new UserService(passwordEncoder, userRepository, null);


  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setName("John");
    user.setSurname("Smith");
    user.setEmail("john@example.com");
    user.setPassword("Qwerty123");
    user.setPhone("1234567890");
  }

  @Test
  void getAllUsers() {}

  @Test
  void getUserById() {}

  @Test
  void getUserByEmail() {}

  @Test
  void getUserByToken() {}

  @Test
  void getUserByTokenWithCountry() {}

  @Test
  void registerUser() {}

  @Test
  void updateUser() {}

  @Test
  void block() {}

  @Test
  void unblock() {}

  @Test
  void isExistsByEmail() {
    Mockito.when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
    Mockito.when(userRepository.existsByEmail("john@example1.com")).thenReturn(false);
    boolean result = userService.isExistsByEmail(user);
    assertTrue(result);
    User user1 = new User();
    user1.setEmail("john@example1.com");
    result = userService.isExistsByEmail(user1);
    assertFalse(result);
  }

  @Test
  void updateUserLastLogin() {}
}
