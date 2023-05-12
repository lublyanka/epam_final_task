package com.example.cards.controllers.billing;

import static com.example.cards.enums.Responses.*;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The Credit card controller. */
@RestController
@RequestMapping("/api/card")
public class CreditCardController {

  @Autowired CreditCardService creditCardService;

  @Autowired AccountService accountService;

  /**
   * Add credit card response entity.
   *
   * @param token the JWT token
   * @param creditCard the credit card
   * @return the response entity
   */
  @PostMapping("/addCard")
  public ResponseEntity<?> addCreditCard(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CreditCard creditCard) {
    Optional<Account> accountOptional =
        accountService.getOptionalAccount(token, creditCard.getAccountId());

    if (accountOptional.isEmpty()) {
      return ACCOUNT_DOES_NOT_EXIST;
    }

    try {
      boolean isDueDateAfterMonthStart = creditCardService.isDueDateAfterMonthStart(creditCard);
      if (!isDueDateAfterMonthStart) return INVALID_DUE_DATE;
    } catch (NumberFormatException e) {
      return INVALID_DUE_DATE;
    }

    if (!creditCardService.isValidCreditCardNumber(creditCard.getCardNumber()))
      return INVALID_CREDIT_CARD_NUMBER;

    creditCard = creditCardService.saveCreditCard(creditCard, accountOptional.get());
    return ResponseEntity.ok(creditCard);
  }

  /**
   * Is valid credit card number response entity.
   *
   * @param token the JWT token
   * @param cardNumber the card number
   * @return the response entity
   */
  @PostMapping("/isValid")
  public ResponseEntity<String> isValidCreditCardNumber(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody String cardNumber) {

    if (cardNumber != null && !cardNumber.isEmpty()) {
      creditCardService.isValidCreditCardNumber(cardNumber);
      return ResponseEntity.status(HttpStatus.OK)
          .body(String.valueOf(creditCardService.isValidCreditCardNumber(cardNumber)));
    } else return INVALID_CREDIT_CARD_NUMBER;
  }
}
