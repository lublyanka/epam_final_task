package com.example.cards.controllers.admin;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.services.AccountService;
import com.example.cards.services.PaymentService;
import com.example.cards.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.cards.enums.Responses.ACCOUNT_DOES_NOT_EXIST;
import static com.example.cards.enums.Responses.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
  @Mock
  PaymentService paymentService;
  @Mock
  UserService userService;
  @Mock private AccountService accountService;
  @InjectMocks private AdminController adminController;

  @Test
  public void testLoadAccounts() {
    String token = "token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Account> accountList = new ArrayList<>();
    accountList.add(new Account());
    accountList.add(new Account());

    Page<Account> accountsPage = new PageImpl<>(accountList);

    when(accountService.getAllAccounts(sortBy, sortOrder, page, size)).thenReturn(accountsPage);

    ResponseEntity<?> response =
        adminController.loadAccounts(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(accountsPage, response.getBody());
  }

  @Test
  public void testLoadAccounts_NoContent() {
    String token = "token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Page<Account> accountsPage = Page.empty();

    when(accountService.getAllAccounts(sortBy, sortOrder, page, size)).thenReturn(accountsPage);

    ResponseEntity<?> response =
        adminController.loadAccounts(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
  @Test
  public void testLoadPayments() {
    String token = "token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Payment> paymentList = new ArrayList<>();
    paymentList.add(new Payment());
    paymentList.add(new Payment());

    Page<Payment> paymentsPage = new PageImpl<>(paymentList);
    when(paymentService.getAllPayments(sortBy, sortOrder, page, size)).thenReturn(paymentsPage);

    ResponseEntity<?> response = adminController.loadPayments(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(paymentsPage, response.getBody());
  }

  @Test
  public void testLoadPayments_NoContent() {
    String token = "token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Page<Payment> paymentsPage = Page.empty();
    when(paymentService.getAllPayments(sortBy, sortOrder, page, size)).thenReturn(paymentsPage);

    ResponseEntity<?> response = adminController.loadPayments(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void testUnblockAccount() {
    String token = "token";
    UUID accountId = UUID.randomUUID();

    Account account = new Account();
    account.setId(accountId);

    Optional<Account> accountOptional = Optional.of(account);
    when(accountService.unblock(token, accountId)).thenReturn(accountOptional);

    ResponseEntity<?> response = adminController.unblockAccount(token, accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals(account, response.getBody());
  }

  @Test
  public void testUnblockAccount_AccountDoesNotExist() {
    String token = "token";
    UUID accountId = UUID.randomUUID();

    Optional<Account> accountOptional = Optional.empty();
    when(accountService.unblock(token, accountId)).thenReturn(accountOptional);

    ResponseEntity<?> response = adminController.unblockAccount(token, accountId);

    assertEquals(ACCOUNT_DOES_NOT_EXIST, response);
  }

  @Test
  public void testLoadUsers_NotEmpty() {
    String token = "token";
    String sortBy = "surname";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<User> userList = new ArrayList<>();
    userList.add(new User());
    userList.add(new User());

    Page<User> users = new PageImpl<>(userList);
    when(userService.getAllUsers(sortBy, sortOrder, page, size)).thenReturn(users);

    ResponseEntity<?> response = adminController.loadUsers(token, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users, response.getBody());
  }

  @Test
  public void testLoadUsers_Empty() {
    String token = "token";
    String sortBy = "surname";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Page<User> users = new PageImpl<>(new ArrayList<>());
    when(userService.getAllUsers(sortBy, sortOrder, page, size)).thenReturn(users);

    ResponseEntity<?> response = adminController.loadUsers(token, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void testLoadUserById_UserExists() {
    String token = "token";
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    when(userService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = adminController.loadUserById(token, userId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(Optional.of(user), response.getBody());
  }

  @Test
  public void testLoadUserById_UserNotFound() {
    String token = "token";
    Long userId = 1L;
    when(userService.getUserById(userId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = adminController.loadUserById(token, userId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testBlockUser_UserExists() {
    // Prepare test data
    String token = "token";
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    when(userService.block(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = adminController.blockUser(token, userId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("User blocked successfully.", response.getBody());
  }

  @Test
  public void testBlockUser_UserNotFound() {
    String token = "token";
    Long userId = 1L;

    when(userService.block(userId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = adminController.blockUser(token, userId);

    assertEquals(USER_NOT_FOUND, response);
  }

  @Test
  public void testUnblockUser_UserExists() {
    String token = "token";
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    when(userService.unblock(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = adminController.unblockUser(token, userId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("User unblocked successfully.", response.getBody());
  }

  @Test
  public void testUnblockUser_UserNotFound() {
    // Prepare test data
    String token = "token";
    Long userId = 1L;
    when(userService.unblock(userId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = adminController.unblockUser(token, userId);

    assertEquals(USER_NOT_FOUND, response);
  }
}


