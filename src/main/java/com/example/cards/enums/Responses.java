package com.example.cards.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responses {
  public static final ResponseEntity<String> ACCOUNT_DOES_NOT_EXIST =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist.");
  public static final ResponseEntity<String> CREDIT_CARD_DOES_NOT_EXIST =
      ResponseEntity.badRequest().body("Credit card does not exist");
  public static final ResponseEntity<String> EMAIL_ALREADY_EXISTS =
      ResponseEntity.status(409).body("Email already exists.");
  public static final ResponseEntity<String> INVALID_CREDIT_CARD_NUMBER =
      ResponseEntity.badRequest().body("Invalid credit card number.");
  public static final ResponseEntity<String> INVALID_EMAIL_OR_PASSWORD =
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
  public static final ResponseEntity<String> PAYMENT_NOT_FOUND =
          ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment does not exist");
  public static final ResponseEntity<String> USER_NOT_FOUND =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
  public static final ResponseEntity<String> NOT_ENOUGH_MONEY =
  ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Balance is no enough to proceed with payment");
}
