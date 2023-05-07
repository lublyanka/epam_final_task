package com.example.cards;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
@CommonsLog
public class CardsApplication {

  public static void main(String[] args) {
    try {
      ConfigurableEnvironment environment = new StandardEnvironment();
      SpringApplication app = new SpringApplication(CardsApplication.class);
      app.setEnvironment(environment);
      //Arrays.stream(args).forEach (log::info);
     // System.getenv().keySet().forEach (log::info);
      log.info("I am alive");
      app.run(args);} catch (Exception e) {
      log.error("Failed starting " + e + e.getMessage());}
    //SpringApplication.run(CardsApplication.class, args);
  }
}
