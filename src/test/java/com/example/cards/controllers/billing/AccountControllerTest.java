package com.example.cards.controllers.billing;

import static com.example.cards.enums.Responses.ACCOUNT_DOES_NOT_EXIST;
import static com.example.cards.enums.Responses.CREDIT_CARD_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.entities.Payment;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;
import com.example.cards.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
  @Mock private PaymentService paymentService;
  @Mock private AccountService accountService;
  @Mock private CreditCardService creditCardService;

  @InjectMocks private AccountController accountController;

  @Test
  public void testLoadUserAccounts() {
    String token = "mock-token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Page<Account> mockPage = new PageImpl<>(Collections.emptyList());
    when(accountService.getAllUserAccounts(isBlocked, sortBy, sortOrder, page, size, token))
        .thenReturn(mockPage);

    ResponseEntity<?> response =
        accountController.loadUserAccounts(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testLoadUserAccounts_NonEmptyAccounts_ReturnsAccounts() {
    String token = "mock-token";
    String isBlocked = "false";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Account> mockAccounts = new ArrayList<>();
    Account account1 = new Account();
    account1.setId(UUID.randomUUID());
    Account account2 = new Account();
    account2.setId(UUID.randomUUID());
    mockAccounts.add(account1);
    mockAccounts.add(account2);

    Page<Account> mockPage = new PageImpl<>(mockAccounts);
    when(accountService.getAllUserAccounts(isBlocked, sortBy, sortOrder, page, size, token))
        .thenReturn(mockPage);

    ResponseEntity<?> response =
        accountController.loadUserAccounts(token, isBlocked, sortBy, sortOrder, page, size);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody() instanceof Page);
    assertEquals(mockPage, response.getBody());
  }

  @Test
  public void testLoadAccountById_ExistingAccount_ReturnsAccount() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();

    Account mockAccount = new Account();

    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));

    ResponseEntity<?> response = accountController.loadAccountById(token, accountId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody() instanceof Account);
    assertEquals(mockAccount, response.getBody());
  }

  @Test
  public void testLoadAccountById_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();

    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.loadAccountById(token, accountId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getStatusCode(), response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testBlockAccount_ExistingAccount_ReturnsNoContent() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    Account mockAccount = new Account();
    when(accountService.block(token, accountId)).thenReturn(Optional.of(mockAccount));

    ResponseEntity<?> response = accountController.blockAccount(token, accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testBlockAccount_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    when(accountService.block(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.blockAccount(token, accountId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testUnblockAccount_ExistingAccount_ReturnsNoContent() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    Account mockAccount = new Account();
    when(accountService.unblockRequest(token, accountId)).thenReturn(Optional.of(mockAccount));

    ResponseEntity<?> response = accountController.unblockAccount(token, accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testUnblockAccount_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    when(accountService.unblockRequest(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.unblockAccount(token, accountId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testLoadAccountCreditCards_ExistingAccountWithCreditCards_ReturnsCreditCardsList() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    Account mockAccount = new Account();
    List<CreditCard> mockCreditCards = Arrays.asList(new CreditCard(), new CreditCard());
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(creditCardService.getCreditCards(mockAccount)).thenReturn(mockCreditCards);

    ResponseEntity<?> response = accountController.loadAccountCreditCards(token, accountId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCreditCards, response.getBody());
  }

  @Test
  public void testLoadAccountCreditCards_ExistingAccountWithoutCreditCards_ReturnsNoContent() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    Account mockAccount = new Account();
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(creditCardService.getCreditCards(mockAccount)).thenReturn(Collections.emptyList());

    ResponseEntity<?> response = accountController.loadAccountCreditCards(token, accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testLoadAccountCreditCards_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.loadAccountCreditCards(token, accountId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testLoadCreditCard_ExistingAccountAndCreditCard_ReturnsCreditCard() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String cardNumber = "1111-1111-1111-1111";
    Account mockAccount = new Account();
    CreditCard mockCreditCard = new CreditCard();
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(creditCardService.getCreditCard(cardNumber, mockAccount))
        .thenReturn(Optional.of(mockCreditCard));

    ResponseEntity<?> response = accountController.loadCreditCard(token, accountId, cardNumber);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockCreditCard, response.getBody());
  }

  @Test
  public void testLoadCreditCard_ExistingAccountNoCreditCard_ReturnsCreditCardDoesNotExist() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String cardNumber = "1111-1111-1111-1111";
    Account mockAccount = new Account();
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(creditCardService.getCreditCard(cardNumber, mockAccount)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.loadCreditCard(token, accountId, cardNumber);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(CREDIT_CARD_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testLoadCreditCard_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String cardNumber = "1111-1111-1111-1111";
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.loadCreditCard(token, accountId, cardNumber);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testLoadAccountPayments_ExistingAccountWithPayments_ReturnsPaymentsList() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;
    Account mockAccount = new Account();

    List<Payment> mockPayments = Arrays.asList(new Payment(), new Payment());

    Page<Payment> mockPaymentPage = new PageImpl<>(mockPayments);
    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(paymentService.getAllAccountPayments(sortBy, sortOrder, page, size, mockAccount))
        .thenReturn(mockPaymentPage);

    ResponseEntity<?> response =
        accountController.loadAccountPayments(token, sortBy, sortOrder, page, size, accountId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockPaymentPage, response.getBody());
  }

  @Test
  public void testLoadAccountPayments_ExistingAccountWithoutPayments_ReturnsNoContent() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Account mockAccount = new Account();
    Page<Payment> mockEmptyPaymentPage = new PageImpl<>(Collections.emptyList());

    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.of(mockAccount));
    when(paymentService.getAllAccountPayments(sortBy, sortOrder, page, size, mockAccount))
        .thenReturn(mockEmptyPaymentPage);

    ResponseEntity<?> response =
        accountController.loadAccountPayments(token, sortBy, sortOrder, page, size, accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testLoadAccountPayments_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    when(accountService.getOptionalAccount(token, accountId)).thenReturn(Optional.empty());

    ResponseEntity<?> response =
        accountController.loadAccountPayments(token, sortBy, sortOrder, page, size, accountId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testReplenishAccount_ValidAmount_ReturnsReplenishedAccount() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String amountStr = "100.50";
    BigDecimal amount = new BigDecimal(amountStr);

    Account mockAccount = new Account();
    when(accountService.isAmountNumeric(amountStr)).thenReturn(true);
    when(accountService.isAmountPositive(amount)).thenReturn(true);
    when(accountService.refillAccount(token, accountId, amount))
        .thenReturn(Optional.of(mockAccount));

    ResponseEntity<?> response = accountController.replenishAccount(token, accountId, amountStr);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockAccount, response.getBody());
  }

  @Test
  public void testReplenishAccount_InvalidAmountFormat_ReturnsBadRequest() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String amountStr = "abc";
    when(accountService.isAmountNumeric(amountStr)).thenReturn(false);

    ResponseEntity<?> response = accountController.replenishAccount(token, accountId, amountStr);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Refilling sum is not numeric.", response.getBody());
  }

  @Test
  public void testReplenishAccount_NegativeAmount_ReturnsBadRequest() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String amountStr = "-50";
    BigDecimal amount = new BigDecimal(amountStr);
    when(accountService.isAmountNumeric(amountStr)).thenReturn(true);
    when(accountService.isAmountPositive(amount)).thenReturn(false);

    ResponseEntity<?> response = accountController.replenishAccount(token, accountId, amountStr);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Refilling sum can't be zero or negative.", response.getBody());
  }

  @Test
  public void testReplenishAccount_ZeroAmount_ReturnsBadRequest() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String amountStr = "0";
    BigDecimal amount = new BigDecimal(amountStr);

    when(accountService.isAmountNumeric(amountStr)).thenReturn(true);
    when(accountService.isAmountPositive(amount)).thenReturn(false);

    ResponseEntity<?> response = accountController.replenishAccount(token, accountId, amountStr);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Refilling sum can't be zero or negative.", response.getBody());
  }

  @Test
  public void testReplenishAccount_NonExistingAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";
    UUID accountId = UUID.randomUUID();
    String amountStr = "100.50";
    BigDecimal amount = new BigDecimal(amountStr);

    when(accountService.isAmountNumeric(amountStr)).thenReturn(true);
    when(accountService.isAmountPositive(amount)).thenReturn(true);
    when(accountService.refillAccount(token, accountId, amount)).thenReturn(Optional.empty());

    ResponseEntity<?> response = accountController.replenishAccount(token, accountId, amountStr);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(ACCOUNT_DOES_NOT_EXIST.getBody(), response.getBody());
  }

  @Test
  public void testCreateAccount_ValidAccount_ReturnsCreatedAccount() {
    String token = "mock-token";
    Account mockAccount = new Account();

    when(accountService.isAccountCurrencyPresent(mockAccount)).thenReturn(true);
    when(accountService.saveAccount(mockAccount, token)).thenReturn(mockAccount);

    ResponseEntity<?> response = accountController.createAccount(token, mockAccount);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockAccount, response.getBody());
  }

  @Test
  public void testCreateAccount_InvalidCurrencyCode_ReturnsBadRequest() {
    String token = "mock-token";
    Account mockAccount = new Account();
    when(accountService.isAccountCurrencyPresent(mockAccount)).thenReturn(false);

    ResponseEntity<?> response = accountController.createAccount(token, mockAccount);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Currency code does not exist.", response.getBody());
  }
}
