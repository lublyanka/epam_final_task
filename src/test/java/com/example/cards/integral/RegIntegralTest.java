package com.example.cards.integral;

import com.example.cards.controllers.auth.RegistrationController;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegIntegralTest {
  @Autowired private RegistrationController registrationController;
  @Autowired UserRepository  userRepository;

  @BeforeAll
  public static void init() {



  }

  @Test
  void contextLoads() {
    userRepository.deleteAllInBatch(); //TODO this is unsafe
    User user = new User();
    user.setName("John");
    user.setSurname("Smith");
    user.setEmail("john@example.com");
    user.setPassword("Qwerty123");
    user.setPhone("1234567890");

    ResponseEntity<?> response = registrationController.registerUser(user);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    User savedUser = (User) response.getBody();
    assertNotNull(savedUser);
    user.setId(savedUser.getId());
    assertEquals(user, savedUser);
    Optional<User> optionalUser = userRepository.findById(user.getId());
    assertTrue(optionalUser.isPresent());
    assertEquals(user, optionalUser.get());
  }
}
