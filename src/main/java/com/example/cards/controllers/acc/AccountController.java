package com.example.cards.controllers.acc;

import static com.example.cards.enums.Responses.*;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

  @Autowired private AccountService accountService;
  @Autowired private CreditCardService creditCardService;

  @GetMapping("/all")
  public ResponseEntity<?> loadUserAccounts(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false, defaultValue = "number") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(required = false, defaultValue = "0")int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Page<Account> accounts = accountService.getAllUserAccounts(sortBy, sortOrder, page, size, token);
    if (!(accounts.isEmpty())) return ResponseEntity.ok(accounts);
    else return ResponseEntity.noContent().build();
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<?> loadAccountById(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {

    Optional<Account> accountOptional = accountService.getOptionalAccount(token, accountId);

    if (accountOptional.isPresent()) return ResponseEntity.ok(accountOptional.get());
    return ACCOUNT_DOES_NOT_EXIST;
  }

  @PutMapping("/{accountId}/block")
  public ResponseEntity<?> blockAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {

    Optional<Account> accountOptional = accountService.block(token, accountId);

    if (accountOptional.isPresent()) {
      return ResponseEntity.noContent().build();
    } else {
      return ACCOUNT_DOES_NOT_EXIST;
    }
  }

  // TODO rewrite to block request
  @PutMapping("/{accountId}/unblock")
  public ResponseEntity<?> unblockAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {
    Optional<Account> accountOptional = accountService.unblockRequest(token, accountId);

    if (accountOptional.isPresent()) {
      return ResponseEntity.noContent().build();
    } else {
      return ACCOUNT_DOES_NOT_EXIST;
    }
  }

  @GetMapping("/{accountId}/cards")
  public ResponseEntity<?> loadAccountCreditCards(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {
    Optional<Account> accountOptional = accountService.getOptionalAccount(token, accountId);

    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      List<CreditCard> creditCards = creditCardService.getCreditCards(account);

      if (!(creditCards.isEmpty())) {
        return ResponseEntity.ok(creditCards);
      } else {
        return ResponseEntity.noContent().build();
      }
    }
    return ACCOUNT_DOES_NOT_EXIST;
  }

  @GetMapping("/{accountId}/{cardNumber}")
  public ResponseEntity<?> loadCreditCard(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable UUID accountId,
      @PathVariable String cardNumber) {

    Optional<Account> accountOptional = accountService.getOptionalAccount(token, accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<CreditCard> creditCardOptional =
          creditCardService.getCreditCard(cardNumber, account);
      if (creditCardOptional.isPresent()) {
        return ResponseEntity.ok(creditCardOptional.get());
      } else {
        return CREDIT_CARD_DOES_NOT_EXIST;
      }
    } else return ACCOUNT_DOES_NOT_EXIST;
  }

  @PostMapping("/{accountId}/refill")
  public ResponseEntity<?> replenishAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable UUID accountId,
      @RequestBody String amountStr) {
    if (!amountStr.matches("^\\d+(\\.|,){0,1}\\d{0,2}$"))
      return ResponseEntity.badRequest().body("Refilling sum is not numeric.");
    BigDecimal amount = new BigDecimal(amountStr);
    if (amount.compareTo(BigDecimal.ZERO) != 1) {
      return ResponseEntity.badRequest().body("Refilling sum can't be zero or negative.");
    }
    Optional<Account> optionalAccount = accountService.refillAccount(token, accountId, amount);
    if (optionalAccount.isEmpty()) {
      return ACCOUNT_DOES_NOT_EXIST;
    } else {
      return ResponseEntity.ok(optionalAccount.get());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Account account) {

    if (!accountService.isAccountCurrencyPresent(account))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency code does not exist.");

    return ResponseEntity.ok(accountService.saveAccount(account, token));
  }

  @PostMapping("/addCard")
  public ResponseEntity<?> addCreditCard(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CreditCard creditCard) {
    Optional<Account> accountOptional =
        accountService.getOptionalAccount(token, creditCard.getAccountId());

    if (accountOptional.isEmpty()) {
      return ACCOUNT_DOES_NOT_EXIST;
    }

    if (!creditCardService.isValidCreditCardNumber(creditCard.getCardNumber()))
      return INVALID_CREDIT_CARD_NUMBER;

    if (!creditCardService.isCreditCardTypePresent(creditCard))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Type does not exist.");

    creditCard = creditCardService.saveCreditCard(creditCard, accountOptional.get());
    return ResponseEntity.ok(creditCard);
  }
}
