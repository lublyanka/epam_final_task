package com.example.cards;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

/**
 * The Cards application entry point.
 */
@SpringBootApplication
@CommonsLog
public class CardsApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    try {
      ConfigurableEnvironment environment = new StandardEnvironment();
      SpringApplication app = new SpringApplication(CardsApplication.class);
      app.setEnvironment(environment);
      log.info("I am alive");
      app.run(args);} catch (Exception e) {
      log.error("Failed starting " + e + e.getMessage());}
  }
}
