package com.example.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
public class CardsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CardsApplication.class, args);
  }
}
