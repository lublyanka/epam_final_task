package com.example.cards.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** The ResponseEntity constants. */
public class Responses {
  /** The constant ACCOUNT_DOES_NOT_EXIST. */
  public static final ResponseEntity<String> ACCOUNT_DOES_NOT_EXIST =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist.");
  /** The constant CREDIT_CARD_DOES_NOT_EXIST. */
  public static final ResponseEntity<String> CREDIT_CARD_DOES_NOT_EXIST =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card does not exist");
  /** The constant EMAIL_ALREADY_EXISTS. */
  public static final ResponseEntity<String> EMAIL_ALREADY_EXISTS =
      ResponseEntity.status(409).body("Email already exists.");
  /** The constant INVALID_CREDIT_CARD_NUMBER. */
  public static final ResponseEntity<String> INVALID_CREDIT_CARD_NUMBER =
      ResponseEntity.badRequest().body("Invalid credit card number.");

  /** The constant INVALID_DUE_DATE. */
  public static final ResponseEntity<String> INVALID_DUE_DATE =
      ResponseEntity.badRequest().body("Invalid due date.");
  /** The constant INVALID_EMAIL_OR_PASSWORD. */
  public static final ResponseEntity<String> INVALID_EMAIL_OR_PASSWORD =
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
  /** The constant USER_IS_BLOCKED. */
  public static final ResponseEntity<String> USER_IS_BLOCKED =
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is blocked, please contact admin");
  /** The constant PAYMENT_NOT_FOUND. */
  public static final ResponseEntity<String> PAYMENT_NOT_FOUND =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment does not exist");
  /** The constant USER_NOT_FOUND. */
  public static final ResponseEntity<String> USER_NOT_FOUND =
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
  /** The constant NOT_ENOUGH_MONEY. */
  public static final ResponseEntity<String> NOT_ENOUGH_MONEY =
      ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
          .body("Balance is no enough to proceed with payment");
  /** The constant REQUIRED_FIELDS_ARE_EMPTY. */
  public static final ResponseEntity<String> REQUIRED_FIELDS_ARE_EMPTY =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required fields are empty");
  /** The constant EMAIL_IS_EMPTY. */
  public static final ResponseEntity<String> EMAIL_IS_EMPTY =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
  /** The constant PASSWORD_IS_EMPTY. */
  public static final ResponseEntity<String> PASSWORD_IS_EMPTY =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");

  /** The constant NAME_IS_EMPTY. */
  public static final ResponseEntity<String> NAME_IS_EMPTY =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Surname and name are required");

  /** The constant PHONE_IS_INVALID. */
  public static final ResponseEntity<String> PHONE_IS_INVALID =
      ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Must contain only numbers from 5 to 20 characters");

  /** The constant PASSWORD_IS_INVALID. */
  public static final ResponseEntity<String> PASSWORD_IS_INVALID =
      ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              "Password must be at least 8 characters long, contain at least one lower case letter, "
                  + "one uppercase letter and one number");

  /** The constant NAME_IS_INVALID. */
  public static final ResponseEntity<String> NAME_IS_INVALID =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name must contain only letters");

  /** The constant SURNAME_IS_INVALID. */
  public static final ResponseEntity<String> SURNAME_IS_INVALID =
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Surname must contain only letters");

  /** The constant EMAIL_IS_INVALID. */
  public static final ResponseEntity<String> EMAIL_IS_INVALID =
      ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              "Email must contain a prefix, @ and an email domain. For example: example@mail.com");
}
