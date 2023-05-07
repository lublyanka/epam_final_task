package com.example.cards.controllers.billing;

import com.example.cards.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.cards.enums.Responses.INVALID_CREDIT_CARD_NUMBER;

@RestController
@RequestMapping("/api/card")
public class CreditCardController {
  @Autowired CreditCardService creditCardService;

  @PostMapping("/isValid")
  public ResponseEntity<String> isValidCreditCardNumber(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody String cardNumber) {

    if (cardNumber != null && !cardNumber.isEmpty()) {
      creditCardService.isValidCreditCardNumber(cardNumber);
      return ResponseEntity.status(HttpStatus.OK).
              body(String.valueOf(creditCardService.isValidCreditCardNumber(cardNumber)));
    } else return INVALID_CREDIT_CARD_NUMBER;
  }
}
