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
  public static final ResponseEntity<String> USER_IS_BLOCKED =
          ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is blocked, please contact admin");
  public static final ResponseEntity<String> PAYMENT_NOT_FOUND =
          ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment does not exist");
  public static final ResponseEntity<String> USER_NOT_FOUND =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
  public static final ResponseEntity<String> NOT_ENOUGH_MONEY =
  ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Balance is no enough to proceed with payment");
  public static final ResponseEntity<String> REQUIRED_FIELDS_ARE_EMPTY =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required fields are empty");
  public static final ResponseEntity<String> EMAIL_IS_EMPTY =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
  public static final ResponseEntity<String> PASSWORD_IS_EMPTY =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");

  public static final ResponseEntity<String> NAME_IS_EMPTY =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Surname and name are required");

  public static final ResponseEntity<String> PHONE_IS_INVALID =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Must contain only numbers from 5 to 20 characters");

  public static final ResponseEntity<String> PASSWORD_IS_INVALID =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long, contain at least one lower case letter, one uppercase letter and one number");

  public static final ResponseEntity<String> NAME_IS_INVALID =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name must contain only letters");

  public static final ResponseEntity<String> SURNAME_IS_INVALID =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Surname must contain only letters");

  public static final ResponseEntity<String> EMAIL_IS_INVALID =
          ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email must contain a prefix, @ and an email domain. For example: example@mail.com");
}
