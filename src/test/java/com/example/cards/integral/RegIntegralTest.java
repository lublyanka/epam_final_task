package com.example.cards.integral;

import com.example.cards.controllers.auth.RegistrationController;
import com.example.cards.entities.User;
import com.example.cards.repositories.UserRepository;
import com.example.cards.services.CaptchaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegIntegralTest {
  @Autowired UserRepository userRepository;
  @Autowired private RegistrationController registrationController;

  @BeforeEach
  public void init() {
    userRepository
        .deleteAllInBatch(); // TODO this is unsafe better add here instantiation of docker test
                             // postgress
  }

  @Test
  void contextLoads() throws IOException {
    try (MockedStatic<CaptchaService> utilities = Mockito.mockStatic(CaptchaService.class)) {
      User user = new User();
      user.setName("John");
      user.setSurname("Smith");
      user.setEmail("john@example.com");
      user.setPassword("Qwerty123");
      user.setPhone("1234567890");

      String gRecaptchaResponse = "gRecaptchaResponse";
      utilities.when(() -> CaptchaService.verify(gRecaptchaResponse)).thenReturn(Boolean.TRUE);

      ResponseEntity<?> response = registrationController.registerUser(user, gRecaptchaResponse);
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
}
