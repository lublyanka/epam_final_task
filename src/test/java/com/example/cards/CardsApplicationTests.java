/*
package com.example.cards;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CardsApplicationTests {

  @Mock private Logger log;
  private MockedStatic<LogManager> mockStatic;

  @BeforeEach
  public void setUp() {
    mockStatic = Mockito.mockStatic(LogManager.class);
    extracted();
  }

  private void extracted() {
    mockStatic.when(() -> LogManager.getLogger(CardsApplication.class))
            .thenReturn(log);
  }

  @AfterEach
  public void tearDown() {
    mockStatic.close();
  }

  @Test
  public void testMain() {

    CardsApplication.main(new String[] {});
    // Verify that "I am alive" message is logged on startup
    verify(log).info("I am alive");
  }
}
*/
