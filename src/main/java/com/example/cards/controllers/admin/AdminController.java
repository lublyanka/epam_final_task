package com.example.cards.controllers.admin;

import static com.example.cards.enums.Responses.ACCOUNT_DOES_NOT_EXIST;
import static com.example.cards.enums.Responses.USER_NOT_FOUND;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.services.AccountService;
import com.example.cards.services.PaymentService;
import com.example.cards.services.UserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** The Admin controller. */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority ('ROLE_ADMIN') ")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class AdminController {

  @Autowired AccountService accountService;

  @Autowired PaymentService paymentService;

  @Autowired UserService userService;

  /**
   * Load accounts response entity.
   *
   * @param token the JWT token
   * @param isBlocked whether the account is blocked
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @return the response entity
   */
  @GetMapping("/accounts/all")
  public ResponseEntity<?> loadAccounts(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false) String isBlocked,
      @RequestParam(required = false, defaultValue = "number") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Page<Account> accounts = accountService.getAllAccounts(sortBy, sortOrder, page, size);
    if (!(accounts.isEmpty())) return ResponseEntity.ok(accounts);
    else return ResponseEntity.noContent().build();
  }

  /**
   * Load payments response entity.
   *
   * @param token the JWT token
   * @param isBlocked whether the account is blocked
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @return the response entity
   */
  @GetMapping("/payments/all")
  public ResponseEntity<?> loadPayments(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false) String isBlocked,
      @RequestParam(required = false, defaultValue = "number") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Page<Payment> payments = paymentService.getAllPayments(sortBy, sortOrder, page, size);
    if (!(payments.isEmpty())) return ResponseEntity.ok(payments);
    else return ResponseEntity.noContent().build();
  }

  /**
   * Unblock account response entity.
   *
   * @param token the JWT token
   * @param accountId the account id
   * @return the response entity
   */
  @PutMapping("/accounts/{accountId}/unblock")
  public ResponseEntity<?> unblockAccount(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {
    Optional<Account> accountOptional = accountService.unblock(token, accountId);

    if (accountOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountOptional.get());
    } else {
      return ACCOUNT_DOES_NOT_EXIST;
    }
  }

  /**
   * Load users response entity.
   *
   * @param token the JWT token
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @return the response entity
   */
  @GetMapping("/users/all")
  public ResponseEntity<?> loadUsers(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false, defaultValue = "surname") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Page<User> users = userService.getAllUsers(sortBy, sortOrder, page, size);
    if (!(users.isEmpty())) return ResponseEntity.ok(users);
    else return ResponseEntity.noContent().build();
  }

  /**
   * Load user by id response entity.
   *
   * @param token the JWT token
   * @param userId the user id
   * @return the response entity
   */
  @GetMapping("/users/{userId}")
  public ResponseEntity<?> loadUserById(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {
    Optional<User> user = userService.getUserById(userId);
    if (!(user.isEmpty())) return ResponseEntity.ok(user);
    else return ResponseEntity.notFound().build();
  }

  /**
   * Block user response entity.
   *
   * @param token the JWT token
   * @param userId the user id
   * @return the response entity
   */
  @PutMapping("/users/{userId}/block")
  public ResponseEntity<?> blockUser(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {

    Optional<User> userOptional = userService.block(userId);

    if (userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User blocked successfully.");
    } else {
      return USER_NOT_FOUND;
    }
  }

  /**
   * Unblock user response entity.
   *
   * @param token the JWT token
   * @param userId the user id
   * @return the response entity
   */
  @PutMapping("/users/{userId}/unblock")
  public ResponseEntity<?> unblockUser(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long userId) {

    Optional<User> userOptional = userService.unblock(userId);

    if (userOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User unblocked successfully.");
    } else {
      return USER_NOT_FOUND;
    }
  }
}
