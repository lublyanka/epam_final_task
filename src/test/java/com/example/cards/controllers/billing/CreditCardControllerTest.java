package com.example.cards.controllers.billing;

import static com.example.cards.enums.Responses.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CreditCardControllerTest {
  @Mock private CreditCardService creditCardService;

  @Mock private AccountService accountService;

  @InjectMocks private CreditCardController creditCardController;

  @Test
  public void testAddCreditCard_ValidData() {
    String token = "token";
    CreditCard creditCard = new CreditCard();
    creditCard.setAccountId(UUID.randomUUID());
    Account account = new Account();

    when(accountService.getOptionalAccount(token, creditCard.getAccountId()))
        .thenReturn(Optional.of(account));
    when(creditCardService.isDueDateAfterMonthStart(creditCard)).thenReturn(true);
    when(creditCardService.isValidCreditCardNumber(creditCard.getCardNumber())).thenReturn(true);
    when(creditCardService.saveCreditCard(creditCard, account)).thenReturn(creditCard);

    ResponseEntity<?> response = creditCardController.addCreditCard(token, creditCard);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(creditCard, response.getBody());
  }

  @Test
  public void testAddCreditCard_AccountDoesNotExist() {
    String token = "token";
    CreditCard creditCard = new CreditCard();
    creditCard.setAccountId(UUID.randomUUID());

    when(accountService.getOptionalAccount(token, creditCard.getAccountId()))
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = creditCardController.addCreditCard(token, creditCard);

    assertEquals(ACCOUNT_DOES_NOT_EXIST, response);
  }

  @Test
  public void testAddCreditCard_InvalidDueDate() {
    String token = "token";
    CreditCard creditCard = new CreditCard();
    creditCard.setAccountId(UUID.randomUUID());
    Account account = new Account();

    when(accountService.getOptionalAccount(token, creditCard.getAccountId()))
        .thenReturn(Optional.of(account));
    when(creditCardService.isDueDateAfterMonthStart(creditCard)).thenReturn(false);

    ResponseEntity<?> response = creditCardController.addCreditCard(token, creditCard);

    assertEquals(INVALID_DUE_DATE, response);
  }

  @Test
  public void testAddCreditCard_InvalidCreditCardNumber() {
    String token = "token";
    CreditCard creditCard = new CreditCard();
    creditCard.setAccountId(UUID.randomUUID());
    Account account = new Account();

    when(accountService.getOptionalAccount(token, creditCard.getAccountId()))
        .thenReturn(Optional.of(account));
    when(creditCardService.isDueDateAfterMonthStart(creditCard)).thenReturn(true);
    when(creditCardService.isValidCreditCardNumber(creditCard.getCardNumber())).thenReturn(false);

    ResponseEntity<?> response = creditCardController.addCreditCard(token, creditCard);

    assertEquals(INVALID_CREDIT_CARD_NUMBER, response);
  }

  @Test
  public void testAddCreditCard_NotANumericDueDate() {
    String token = "token";
    CreditCard creditCard = new CreditCard();
    creditCard.setAccountId(UUID.randomUUID());
    Account account = new Account();

    when(accountService.getOptionalAccount(token, creditCard.getAccountId()))
            .thenReturn(Optional.of(account));
    when(creditCardService.isDueDateAfterMonthStart(creditCard)).thenThrow(NumberFormatException.class);

    ResponseEntity<?> response = creditCardController.addCreditCard(token, creditCard);

    assertEquals(INVALID_DUE_DATE, response);
  }
  @Test
  public void testIsValidCreditCardNumber_ValidCardNumber_ReturnsTrue() {
    String token = "Valid token";
    String cardNumber = "1234567890123456";

    ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("true");

    when(creditCardService.isValidCreditCardNumber(cardNumber)).thenReturn(true);
    ResponseEntity<String> response =
        creditCardController.isValidCreditCardNumber(token, cardNumber);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testIsValidCreditCardNumber_InvalidCardNumber_ReturnsFalse() {
    String token = "Valid token";
    String cardNumber = "1234";

    ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("false");
    when(creditCardService.isValidCreditCardNumber(cardNumber)).thenReturn(false);
    ResponseEntity<String> response =
        creditCardController.isValidCreditCardNumber(token, cardNumber);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testIsValidCreditCardNumber_CardNumberEmpty_ReturnsFalse() {
    String token = "mock-token";
    String cardNumber = "";

    ResponseEntity<String> expectedResponse = INVALID_CREDIT_CARD_NUMBER;
    ResponseEntity<String> response =
        creditCardController.isValidCreditCardNumber(token, cardNumber);

    assertEquals(expectedResponse, response);

    response = creditCardController.isValidCreditCardNumber(token, null);

    assertEquals(expectedResponse, response);
  }
}
