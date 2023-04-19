package com.example.cards.controllers.acc;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.entities.User;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;
import com.example.cards.services.UserService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

  public static final ResponseEntity<String> ACCOUNT_DOES_NOT_EXIST =
      ResponseEntity.badRequest().body("Account does not exist.");
  public static final ResponseEntity<String> INVALID_CREDIT_CARD_NUMBER =
      ResponseEntity.badRequest().body("Invalid credit card number.");

  @Autowired private AccountService accountService;
  @Autowired private UserService userService;
  @Autowired private CreditCardService creditCardService;

  @GetMapping("/all")
  public ResponseEntity<?> loadUserAccounts(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false, defaultValue = "number") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
    //List<Account> accounts = accountService.getAllUserAccounts(sortBy, sortOrder, token);
    List<Account> accounts = accountService.getAllUserAccounts2(sortBy, sortOrder,0,10, token);
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
    Optional<Account> accountOptional = accountService.getOptionalAccount(token, accountId);

    if (accountOptional.isPresent()) {
      accountService.block(accountOptional.get());
      return ResponseEntity.ok("Account blocked successfully.");
    } else {
      return ACCOUNT_DOES_NOT_EXIST;
    }
  }

  @PutMapping("/{accountId}/unblock")
  public ResponseEntity<?> unblockAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {
    Optional<Account> accountOptional = accountService.getOptionalAccount(token, accountId);

    if (accountOptional.isPresent()) {
      accountService.unblock(accountOptional.get());
      return ResponseEntity.ok("Account unblocked successfully.");
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

      if (!(creditCards.isEmpty())) return ResponseEntity.ok(creditCards);
      else return ResponseEntity.noContent().build();
    }
    return ACCOUNT_DOES_NOT_EXIST;
  }

  @GetMapping("/{accountId}/{cardNumber}")
  public ResponseEntity<?> loadCreditCard(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable UUID accountId,
      @PathVariable String cardNumber) {

    if (!creditCardService.isValidCreditCardNumber(cardNumber)) return INVALID_CREDIT_CARD_NUMBER;

    User user = userService.getUserByToken(token);

    Optional<Account> accountOptional = accountService.getOptionalAccount(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (accountService.getOptionalAccount(token, account.getId()).isPresent()) {
        Optional<CreditCard> creditCardOptional =
            creditCardService.getCreditCard(cardNumber, account);
        if (creditCardOptional.isPresent()) return ResponseEntity.ok(creditCardOptional.get());
        else return ResponseEntity.badRequest().body("Credit card does not exist");
      } else return ACCOUNT_DOES_NOT_EXIST;
    } else return ACCOUNT_DOES_NOT_EXIST;
  }

  @PostMapping("/{accountId}/refill")
  public ResponseEntity<?> replenishAccount(
      @PathVariable UUID accountId, @RequestParam BigDecimal amount) {
    Optional<Account> optionalAccount = accountService.getOptionalAccount(accountId);

    if (optionalAccount.isEmpty()) {
      return ACCOUNT_DOES_NOT_EXIST;
    }
    if (amount.compareTo(BigDecimal.ZERO) != 1) {
      return ResponseEntity.badRequest().body("Refilling sum can't be zero or negative.");
    }

    accountService.refillAccount(amount, optionalAccount.get());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/create")
  public ResponseEntity<?> createUserAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Account account) {
    User user = userService.getUserByToken(token);

    if (!accountService.isAccountCurrencyPresent(account))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency code does not exist.");

    return ResponseEntity.ok(accountService.saveAccount(account, user));
  }

  @PostMapping("/addCard")
  public ResponseEntity<?> addCreditCard(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CreditCard creditCard) {

    if (!creditCardService.isValidCreditCardNumber(creditCard.getCardNumber()))
      return INVALID_CREDIT_CARD_NUMBER;

    if (!creditCardService.isCreditCardTypePresent(creditCard))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Type does not exist.");

    Optional<Account> accountOptional =
        accountService.getOptionalAccount(creditCard.getAccountId());
    if (accountOptional.isEmpty()) return ACCOUNT_DOES_NOT_EXIST;

    User user = userService.getUserByToken(token);
    accountOptional = accountService.getAccountExistenceByUser(accountOptional.get(), user);
    if (accountOptional.isPresent()) {
      creditCard = creditCardService.saveCreditCard(creditCard, accountOptional.get());
      return ResponseEntity.ok(creditCard);
    }

    return ACCOUNT_DOES_NOT_EXIST;
  }
}
