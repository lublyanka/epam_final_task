package com.example.cards.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import com.example.cards.entities.UserAccount;
import com.example.cards.entities.dict.Currency;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.UserAccountRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

  @Mock private UserService userService;

  @Mock private AccountRepository accountRepository;

  @Mock private UserAccountRepository userAccountRepository;
  @Mock private CurrencyRepository currencyRepository;

  @InjectMocks private AccountService accountService;

  @Test
  public void testGetOptionalAccount_AdminUser_ReturnsAccount() {
    User adminUser = new User();
    adminUser.setRole("ADMIN");

    Account account = new Account();
    UUID accountId = UUID.randomUUID();
    account.setId(accountId);

    when(userService.getUserByToken(anyString())).thenReturn(adminUser);
    when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));

    Optional<Account> result = accountService.getOptionalAccount("validToken", accountId);

    verify(accountRepository).findById(eq(accountId));
    verify(userService).getUserByToken(eq("validToken"));
    assertTrue(result.isPresent());
    assertEquals(account, result.get());
  }

  @Test
  public void testGetOptionalAccount_NonAdminUserWithAccess_ReturnsAccount() {
    User nonAdminUser = new User();
    nonAdminUser.setRole("USER");

    Account account = new Account();
    UUID accountId = UUID.randomUUID();
    account.setId(accountId);

    UserAccount userAccount = mock(UserAccount.class);

    when(userService.getUserByToken(anyString())).thenReturn(nonAdminUser);
    when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(nonAdminUser), eq(account)))
        .thenReturn(Optional.of(userAccount));

    Optional<Account> result = accountService.getOptionalAccount("validToken", accountId);

    verify(accountRepository).findById(eq(accountId));
    verify(userService).getUserByToken(eq("validToken"));
    verify(userAccountRepository)
        .findByUserAccountKeyUserIdAndUserAccountKeyAccountId(eq(nonAdminUser), eq(account));
    assertTrue(result.isPresent());
    assertEquals(account, result.get());
  }

  @Test
  public void testGetOptionalAccount_NonAdminUserWithoutAccess_ReturnsEmpty() {
    User nonAdminUser = new User();
    nonAdminUser.setRole("USER");

    Account account = new Account();
    UUID accountId = UUID.randomUUID();
    account.setId(accountId);

    when(userService.getUserByToken(anyString())).thenReturn(nonAdminUser);
    when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(nonAdminUser), eq(account)))
        .thenReturn(Optional.empty());

    Optional<Account> result = accountService.getOptionalAccount("validToken", accountId);

    verify(accountRepository, times(1)).findById(eq(accountId));
    verify(userService, times(1)).getUserByToken(eq("validToken"));
    verify(userAccountRepository, times(1))
        .findByUserAccountKeyUserIdAndUserAccountKeyAccountId(eq(nonAdminUser), eq(account));
    assertFalse(result.isPresent());
  }

  @Test
  public void testGetAllUserAccounts() {
    User user = new User();
    user.setId(1L);

    List<Account> accounts = new ArrayList<>();
    Account account1 = new Account();
    account1.setId(UUID.randomUUID());
    Account account2 = new Account();
    account2.setId(UUID.randomUUID());
    accounts.add(account1);
    accounts.add(account2);

    Page<Account> pageResult = new PageImpl<>(accounts);

    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(accountRepository.findAllByUserIdWithPagination(any(User.class), any(Pageable.class)))
        .thenReturn(pageResult);
    lenient()
        .when(
            accountRepository.findAllByUserIdWithPaginationAndStatus(
                any(User.class), anyBoolean(), any(Pageable.class)))
        .thenReturn(pageResult);

    Page<Account> result =
        accountService.getAllUserAccounts(null, "sortBy", "asc", 0, 10, "validToken");

    verify(userService, times(1)).getUserByToken(eq("validToken"));
    verify(accountRepository, times(1))
        .findAllByUserIdWithPagination(eq(user), any(Pageable.class));
    verify(accountRepository, never())
        .findAllByUserIdWithPaginationAndStatus(any(User.class), anyBoolean(), any(Pageable.class));
    assertEquals(accounts, result.getContent());
  }

  @Test
  void testGetAllUserAccounts_WithStatus() {
    String status = "true";
    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;
    String token = "valid_token";

    User user = new User();
    user.setId(1L);

    List<Account> accounts = new ArrayList<>();
    accounts.add(new Account());
    accounts.add(new Account());
    Page<Account> pageResult = new PageImpl<>(accounts);
    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(accountRepository.findAllByUserIdWithPaginationAndStatus(
            user, true, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy))))
            .thenReturn(pageResult);

    Page<Account> result = accountService.getAllUserAccounts(status, sortBy, sortOrder, page, size, token);

    assertEquals(pageResult, result);
  }
  @Test
  public void testIsAmountNumeric_ValidAmount_ReturnsTrue() {
    String amountStr = "10.50";
    assertTrue(accountService.isAmountNumeric(amountStr));
  }

  @Test
  public void testIsAmountNumeric_InvalidAmount_ReturnsFalse() {
    String amountStr = "ABC";
    assertFalse(accountService.isAmountNumeric(amountStr));
  }

  @Test
  public void testIsAmountPositive_PositiveAmount_ReturnsTrue() {
    BigDecimal amount = new BigDecimal("100.00");
    assertTrue(accountService.isAmountPositive(amount));
  }

  @Test
  public void testIsAmountPositive_NegativeAmount_ReturnsFalse() {
    BigDecimal amount = new BigDecimal("-50.00");
    assertFalse(accountService.isAmountPositive(amount));
  }

  @Test
  public void testIsAmountPositive_ZeroAmount_ReturnsFalse() {
    BigDecimal amount = BigDecimal.ZERO;
    assertFalse(accountService.isAmountPositive(amount));
  }
  @Test
  public void testRefillAccount() {
    String token = "validToken";
    UUID accountId = UUID.fromString("e59c166d-dd39-406b-a27d-408a28a9a643");
    User user = new User();
    user.setId(1L);
    user.setRole("user");

    Account account = new Account();
    account.setId(accountId);
    BigDecimal initialBalance = BigDecimal.valueOf(100);
    account.setCurrentBalance(initialBalance);

    BigDecimal amount = BigDecimal.valueOf(50);

    Optional<Account> accountOptional = Optional.of(account);
    Account account2 = new Account();
    account2.setId(accountId);
    account2.setCurrentBalance(initialBalance.add(amount));

    UserAccount userAccount = mock(UserAccount.class);
    when(accountRepository.findById(accountId)).thenReturn(accountOptional);
    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(user), eq(account)))
        .thenReturn(Optional.ofNullable(userAccount));
    when(accountRepository.save(account)).thenReturn(account2);

    Optional<Account> result = accountService.refillAccount(token, accountId, amount);

    assertEquals(account2, result.orElse(null));

    amount = BigDecimal.valueOf(-1);
    result = accountService.refillAccount(token, accountId, amount);
    assertEquals(account, result.orElse(null));

    amount = BigDecimal.valueOf(0);
    result = accountService.refillAccount(token, accountId, amount);
    assertEquals(account, result.orElse(null));
  }

  @Test
  public void testRefillAccount_notFound() {
    String token = "validToken";
    UUID accountId = UUID.randomUUID();
    Account account = new Account();
    account.setId(accountId);
    Optional<Account> accountOptional = Optional.empty();

    User user = mock(User.class);
    when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
    when(userService.getUserByToken(anyString())).thenReturn(user);

    Optional<Account> result = accountService.refillAccount(token, accountId, null);
    assertEquals(accountOptional, result);
  }

  @Test
  public void testSaveAccount() {
    String token = "validToken";
    Account account = new Account();
    User user = new User();

    when(userService.getUserByToken(eq(token))).thenReturn(user);
    when(accountRepository.save(account)).thenReturn(account);

    Account result = accountService.saveAccount(account, token);
    verify(userService, times(1)).getUserByToken(eq(token));

    account.setCurrentBalance(BigDecimal.ZERO);
    account.setCreatedOn(Timestamp.from(Instant.now()));
    account.setUpdatedOn(Timestamp.from(Instant.now()));

    // verify(accountService, times(1)).updateAccount(eq(account));
    verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    verify(accountRepository, times(1)).flush();

    assertEquals(account, result);
  }

  @Test
  public void testUpdateAccount() {
    Account account = new Account();
    Account account1 = new Account();
    when(accountRepository.save(account)).thenReturn(account1);
    Account result = accountService.updateAccount(account);

    verify(accountRepository, times(1)).save(eq(account));
    assertEquals(account1, result);
  }

  @Test
  public void testBlock() {
    String token = "mockToken";
    UUID accountId = UUID.randomUUID();

    Account account = new Account();
    account.setId(accountId);
    account.setBlocked(false);
    User user = new User();
    user.setId(1L);
    user.setRole("user");

    Optional<Account> accountOptional = Optional.of(account);

    UserAccount userAccount = mock(UserAccount.class);
    when(accountRepository.findById(accountId)).thenReturn(accountOptional);
    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(user), eq(account)))
        .thenReturn(Optional.ofNullable(userAccount));

    Optional<Account> result = accountService.block(token, accountId);

    verify(accountRepository).save(account);
    assertTrue(account.isBlocked());
    assertEquals(accountOptional, result);

    Optional<Account> emptyAccountOptional = Optional.empty();
    when(accountRepository.findById(accountId)).thenReturn(emptyAccountOptional);

    Optional<Account> emptyResult = accountService.block(token, accountId);

    assertEquals(emptyAccountOptional, emptyResult);
  }

  @Test
  public void testUnblockRequest() {
    String token = "mockToken";
    UUID accountId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    user.setRole("user");

    Account account = new Account();
    account.setId(accountId);
    account.setBlocked(true);
    account.setRequested(false);

    Optional<Account> accountOptional = Optional.of(account);

    UserAccount userAccount = mock(UserAccount.class);
    when(accountRepository.findById(accountId)).thenReturn(accountOptional);
    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(user), eq(account)))
        .thenReturn(Optional.ofNullable(userAccount));

    Optional<Account> result = accountService.unblockRequest(token, accountId);

    verify(accountRepository).save(account);
    assertTrue(account.isRequested());
    assertEquals(accountOptional, result);

    Optional<Account> emptyAccountOptional = Optional.empty();
    when(accountRepository.findById(accountId)).thenReturn(emptyAccountOptional);

    Optional<Account> emptyResult = accountService.unblockRequest(token, accountId);

    assertEquals(emptyAccountOptional, emptyResult);
  }

  @Test
  public void testGetComparator() {
    String sortBy = "name";
    String sortOrder = "asc";

    Comparator<Account> comparator = accountService.getComparator(sortBy, sortOrder);

    Account account1 = new Account();
    account1.setName("John");
    Account account2 = new Account();
    account2.setName("Alice");

    int result = comparator.compare(account1, account2);

    assertTrue(result > 0);
  }

  @Test
  public void testGetComparator_Reversed() {
    String sortBy = "name";
    String sortOrder = "desc";

    Comparator<Account> comparator = accountService.getComparator(sortBy, sortOrder);

    Account account1 = new Account();
    account1.setName("John");
    Account account2 = new Account();
    account2.setName("Alice");

    int result = comparator.compare(account1, account2);

    assertTrue(result < 0);
  }

  @Test
  public void testGetComparator_Balance() {
    String sortBy = "balance";
    String sortOrder = "asc";

    Comparator<Account> comparator = accountService.getComparator(sortBy, sortOrder);

    Account account1 = new Account();
    account1.setName("Alice");
    Account account2 = new Account();
    account2.setName("John");
    account1.setCurrentBalance(BigDecimal.valueOf(2000));
    account2.setCurrentBalance(BigDecimal.valueOf(1000));

    int result = comparator.compare(account1, account2);

    assertTrue(result > 0);
  }

  @Test
  public void testGetComparator_Default() {
    String sortBy = "";
    String sortOrder = "asc";

    Comparator<Account> comparator = accountService.getComparator(sortBy, sortOrder);

    Account account1 = new Account();
    account1.setName("Alice");
    Account account2 = new Account();
    account2.setName("John");
    account1.setCurrentBalance(BigDecimal.valueOf(1000));
    account2.setCurrentBalance(BigDecimal.valueOf(2000));
    account1.setNumber("456");
    account2.setNumber("123");

    int result = comparator.compare(account1, account2);

    assertTrue(result > 0);
  }

  @Test
  public void testGetAllCurrencies() {
    Currency currency1 = new Currency("USD", "US Dollar");
    Currency currency2 = new Currency("EUR", "Euro");

    List<Currency> currencies = Arrays.asList(currency1, currency2);
    when(currencyRepository.findAll()).thenReturn(currencies);

    List<Currency> result = accountService.getAllCurrencies();

    assertEquals(currencies, result);
  }

  @Test
  public void testGetAllAccounts() {
    String sortBy = "name";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Account> accounts = new ArrayList<>();
    Account account1 = new Account();
    account1.setNumber("123");
    account1.setCurrentBalance(BigDecimal.valueOf(1000));
    account1.setName("John Doe");
    accounts.add(account1);
    Account account2 = new Account();
    account1.setNumber("456");
    account2.setCurrentBalance(BigDecimal.valueOf(2000));
    account2.setName("Jane Smith");
    accounts.add(account2);

    Page<Account> pageResult = new PageImpl<>(accounts);
    when(accountRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

    Page<Account> result = accountService.getAllAccounts(sortBy, sortOrder, page, size);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(accountRepository).findAll(pageableCaptor.capture());
    Pageable capturedPageable = pageableCaptor.getValue();
    assertEquals(page, capturedPageable.getPageNumber());
    assertEquals(size, capturedPageable.getPageSize());
    assertEquals(
        Sort.Direction.fromString(sortOrder),
        capturedPageable.getSort().getOrderFor(sortBy).getDirection());

    assertEquals(accounts, result.getContent());
  }

  @Test
  public void testUnblock() {
    String token = "token";
    UUID accountId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    user.setRole("user");

    Account blockedAccount = new Account();
    blockedAccount.setBlocked(true);
    blockedAccount.setRequested(true);

    Optional<Account> accountOptional = Optional.of(blockedAccount);
    UserAccount userAccount = mock(UserAccount.class);
    when(accountRepository.findById(accountId)).thenReturn(accountOptional);
    when(userService.getUserByToken(anyString())).thenReturn(user);
    when(userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
            eq(user), eq(blockedAccount)))
        .thenReturn(Optional.ofNullable(userAccount));

    Optional<Account> result = accountService.unblock(token, accountId);

    assertTrue(result.isPresent());
    Account unblockedAccount = result.get();
    assertFalse(unblockedAccount.isBlocked());
    assertFalse(unblockedAccount.isRequested());
    verify(accountRepository).save(blockedAccount);
  }

  @Test
  void isAccountCurrencyPresent() {
    Account account = new Account();
    account.setCurrencyCode("USD");
    when(currencyRepository.existsById("USD")).thenReturn(true);
    assertTrue(accountService.isAccountCurrencyPresent(account));
    when(currencyRepository.existsById("USD")).thenReturn(false);
    assertFalse(accountService.isAccountCurrencyPresent(account));
  }
}
