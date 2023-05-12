package com.example.cards.services;

import static com.example.cards.enums.Responses.NOT_ENOUGH_MONEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.PaymentRepository;
import com.example.cards.requests.PaymentRequest;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class PaymentServiceTest {

  private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
  private final AccountRepository accountRepository = mock(AccountRepository.class);
  private final UserService userService = mock(UserService.class);
  private final PaymentService paymentService =
      new PaymentService(paymentRepository, accountRepository, userService);

  @Test
  void getById() {
    UUID paymentId = UUID.randomUUID();
    Payment payment = new Payment();
    payment.setId(paymentId);
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
    Optional<Payment> result = paymentService.getById(paymentId);
    assertTrue(result.isPresent());
    assertEquals(paymentId, result.get().getId());
  }

  @Test
  public void testGetPayment_AdminUser_ReturnsPayment() {
    String token = "adminToken";
    UUID paymentId = UUID.randomUUID();
    User adminUser = new User();
    adminUser.setRole("ADMIN");
    Payment payment = new Payment();
    payment.setId(paymentId);

    when(userService.getUserByToken(token)).thenReturn(adminUser);
    when(paymentService.getById(paymentId)).thenReturn(Optional.of(payment));
    Optional<Payment> result = paymentService.getPayment(token, paymentId);

    assertTrue(result.isPresent());
    assertEquals(payment, result.get());

    verify(userService, times(1)).getUserByToken(token);
  }

  @Test
  public void testGetPayment_NonAdminUser_MatchingUser_ReturnsPayment() {
    String token = "userToken";
    UUID paymentId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    user.setRole("USER");
    Payment payment = new Payment();
    payment.setId(paymentId);
    payment.setUser(user);

    when(userService.getUserByToken(token)).thenReturn(user);
    when(paymentService.getById(paymentId)).thenReturn(Optional.of(payment));
    Optional<Payment> result = paymentService.getPayment(token, paymentId);

    assertTrue(result.isPresent());
    assertEquals(payment, result.get());

    verify(userService, times(1)).getUserByToken(token);
  }

  @Test
  public void testGetPayment_NonAdminUser_NonMatchingUser_ReturnsEmptyOptional() {
    String token = "userToken";
    UUID paymentId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    user.setRole("USER");
    Payment payment = new Payment();
    payment.setId(paymentId);
    payment.setUser(new User()); // User with different ID

    when(userService.getUserByToken(token)).thenReturn(user);
    when(paymentService.getById(paymentId)).thenReturn(Optional.of(payment));
    Optional<Payment> result = paymentService.getPayment(token, paymentId);

    assertFalse(result.isPresent());
    verify(userService, times(1)).getUserByToken(token);
  }

  @Test
  void getAllUserPayments() {

    String token = "userToken";
    String sortBy = "date";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;
    User user = new User();
    user.setId(1L);
    List<Payment> paymentList = new ArrayList<>();
    paymentList.add(new Payment());
    paymentList.add(new Payment());
    paymentList.add(new Payment());

    when(userService.getUserByToken(token)).thenReturn(user);

    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
    Page<Payment> pageResult = new PageImpl<>(paymentList, pageable, paymentList.size());
    when(paymentRepository.findAllByUser_Id(user.getId(), pageable)).thenReturn(pageResult);

    Page<Payment> result = paymentService.getAllUserPayments(sortBy, sortOrder, page, size, token);

    assertEquals(paymentList.size(), result.getContent().size());

    verify(userService, times(1)).getUserByToken(token);
    verify(paymentRepository, times(1)).findAllByUser_Id(user.getId(), pageable);
  }

  @Test
  void getAllAccountPayments() {
    String sortBy = "date";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;
    Account account = new Account();
    List<Payment> paymentList = new ArrayList<>();
    paymentList.add(new Payment());
    paymentList.add(new Payment());
    paymentList.add(new Payment());

    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
    Page<Payment> pageResult = new PageImpl<>(paymentList, pageable, paymentList.size());
    when(paymentRepository.findAllByAccount(account, pageable)).thenReturn(pageResult);

    Page<Payment> result =
        paymentService.getAllAccountPayments(sortBy, sortOrder, page, size, account);

    assertEquals(paymentList.size(), result.getContent().size());

    verify(paymentRepository, times(1)).findAllByAccount(account, pageable);
  }

  @Test
  void savePayment() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.valueOf(100));
    paymentRequest.setNumber("123456789");
    paymentRequest.setCurrency("USD");
    paymentRequest.setDescription("Test payment");
    String token = "token";
    Account account = new Account();
    account.setCurrentBalance(BigDecimal.valueOf(101L));
    User user = new User();
    Payment savedPayment = new Payment();

    when(userService.getUserByToken(token)).thenReturn(user);

    when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

    Optional<Payment> result = paymentService.savePayment(paymentRequest, token, account);

    assertTrue(result.isPresent());
    assertEquals(savedPayment, result.get());

    verify(userService, times(1)).getUserByToken(token);

    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository, times(1)).save(paymentCaptor.capture());
    Payment savedPaymentArgument = paymentCaptor.getValue();
    assertEquals(account, savedPaymentArgument.getAccount());
    assertEquals(paymentRequest.getNumber(), savedPaymentArgument.getNumber());
    assertEquals(paymentRequest.getAmount(), savedPaymentArgument.getAmount());
    assertEquals(paymentRequest.getCurrency(), savedPaymentArgument.getCurrencyCode());
    assertEquals(paymentRequest.getDescription(), savedPaymentArgument.getDescription());
    assertNotNull(savedPaymentArgument.getCreatedOn());
    assertNotNull(savedPaymentArgument.getUpdatedOn());
    assertEquals(PaymentStatus.PREPARED, savedPaymentArgument.getStatus());
    assertEquals(user, savedPaymentArgument.getUser());
  }

  @Test
  void savePaymentWithNotEnoughMoneyOnCurrenctAccount() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.valueOf(100));
    paymentRequest.setNumber("123456789");
    paymentRequest.setCurrency("USD");
    paymentRequest.setDescription("Test payment");
    String token = "token";
    Account account = new Account();
    account.setCurrentBalance(BigDecimal.valueOf(99L));
    User user = new User();
    Payment savedPayment = new Payment();

    when(userService.getUserByToken(token)).thenReturn(user);

    Optional<Payment> result = paymentService.savePayment(paymentRequest, token, account);

    assertTrue(result.isEmpty());

    verify(userService, never()).getUserByToken(token);

    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository, never()).save(paymentCaptor.capture());
  }

  @Test
  void sendPayment() {
    String token = "token";
    UUID paymentId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    Payment payment = new Payment();
    Account account = new Account();
    BigDecimal initialBalance = BigDecimal.valueOf(200);
    BigDecimal paymentAmount = BigDecimal.valueOf(100);

    payment.setId(paymentId);
    payment.setUser(user);
    payment.setAccount(account);
    payment.setAmount(paymentAmount);
    payment.setStatus(PaymentStatus.PREPARED);

    Payment sentPayment = new Payment();
    sentPayment.setId(paymentId);
    sentPayment.setUser(user);
    sentPayment.setAccount(account);
    sentPayment.setAmount(paymentAmount);
    sentPayment.setStatus(PaymentStatus.SENT);

    account.setId(UUID.randomUUID());
    account.setCurrentBalance(initialBalance);

    when(userService.getUserByToken(token)).thenReturn(user);

    when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
    when(paymentRepository.save(payment)).thenReturn(sentPayment);

    Optional<?> result = paymentService.sendPayment(token, paymentId);

    assertTrue(result.isPresent());
    assertEquals(sentPayment, result.get());

    verify(userService, times(1)).getUserByToken(token);
    verify(paymentRepository, times(1)).findById(paymentId);

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(accountRepository, times(1)).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();
    assertEquals(account.getId(), savedAccount.getId());
    assertEquals(initialBalance.subtract(paymentAmount), savedAccount.getCurrentBalance());

    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository, times(1)).save(paymentCaptor.capture());
    Payment savedPayment = paymentCaptor.getValue();
    assertEquals(payment, savedPayment);
    assertEquals(PaymentStatus.SENT, savedPayment.getStatus());
  }

  @Test
  void sendPaymentWithNotEnoughMoneyOnCurrentBalance() {
    String token = "token";
    UUID paymentId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    Payment payment = new Payment();
    Account account = new Account();
    BigDecimal initialBalance = BigDecimal.valueOf(99);
    BigDecimal paymentAmount = BigDecimal.valueOf(100);

    payment.setId(paymentId);
    payment.setUser(user);
    payment.setAccount(account);
    payment.setAmount(paymentAmount);
    payment.setStatus(PaymentStatus.PREPARED);

    account.setId(UUID.randomUUID());
    account.setCurrentBalance(initialBalance);

    when(userService.getUserByToken(token)).thenReturn(user);
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

    Optional<?> result = paymentService.sendPayment(token, paymentId);

    assertTrue(result.isPresent());
    assertEquals(NOT_ENOUGH_MONEY, result.get());

    verify(userService, times(1)).getUserByToken(token);
    verify(paymentRepository, times(1)).findById(paymentId);

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(accountRepository, never()).save(accountCaptor.capture());

    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository, never()).save(paymentCaptor.capture());
  }

  @Test
  void sendPaymentNoPayment() {
    String token = "token";
    UUID paymentId = UUID.randomUUID();
    User user = new User();
    user.setId(1L);
    Payment payment = new Payment();
    Account account = new Account();
    BigDecimal initialBalance = BigDecimal.valueOf(99);
    BigDecimal paymentAmount = BigDecimal.valueOf(100);

    payment.setId(paymentId);
    payment.setUser(user);
    payment.setAccount(account);
    payment.setAmount(paymentAmount);
    payment.setStatus(PaymentStatus.PREPARED);

    account.setId(UUID.randomUUID());
    account.setCurrentBalance(initialBalance);

    when(userService.getUserByToken(token)).thenReturn(user);

    when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

    Optional<?> result = paymentService.sendPayment(token, paymentId);

    assertTrue(result.isEmpty());

    verify(userService, times(1)).getUserByToken(token);
    verify(paymentRepository, times(1)).findById(paymentId);

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(accountRepository, never()).save(accountCaptor.capture());

    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository, never()).save(paymentCaptor.capture());
  }

  @Test
  void isPaymentSumPositive() {
    Account account = new Account();
    BigDecimal currentBalance = BigDecimal.valueOf(200);
    BigDecimal positiveSum = BigDecimal.valueOf(100);
    BigDecimal zeroSum = BigDecimal.ZERO;
    BigDecimal negativeSum = BigDecimal.valueOf(-50);
    account.setCurrentBalance(currentBalance);

    assertTrue(paymentService.isPaymentSumPositive(positiveSum, account));
    assertFalse(paymentService.isPaymentSumPositive(zeroSum, account));
    assertFalse(paymentService.isPaymentSumPositive(negativeSum, account));
  }

  @Test
  void getComparator() {
    String sortByDate = "date";
    String sortByNumber = "number";
    String sortOrderAsc = "asc";
    String sortOrderDesc = "desc";

    Comparator<Payment> dateAscComparator = paymentService.getComparator(sortByDate, sortOrderAsc);
    Comparator<Payment> dateDescComparator =
        paymentService.getComparator(sortByDate, sortOrderDesc);
    Comparator<Payment> numberAscComparator =
        paymentService.getComparator(sortByNumber, sortOrderAsc);
    Comparator<Payment> numberDescComparator =
        paymentService.getComparator(sortByNumber, sortOrderDesc);

    assertNotNull(dateAscComparator);
    assertNotNull(dateDescComparator);
    assertNotNull(numberAscComparator);
    assertNotNull(numberDescComparator);
  }

  @Test
  @WithMockUser(authorities = "ROLE_ADMIN")
  void getAllPayments() {
    List<Payment> paymentList = new ArrayList<>();
    paymentList.add(new Payment());
    paymentList.add(new Payment());
    paymentList.add(new Payment());
    String sortBy = "date";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
    Page<Payment> pageResult = new PageImpl<>(paymentList, pageable, paymentList.size());
    when(paymentRepository.findAll(pageable)).thenReturn(pageResult);
    Page<Payment> result = paymentService.getAllPayments(sortBy, sortOrder, page, size);

    assertNotNull(result);
    assertEquals(paymentList, result.getContent());
    /*assertThrows(AccessDeniedException.class, () -> {
        paymentService.getAllPayments(sortBy, sortOrder, page, size);
    });*/
  }

  @Test
  public void testGetStatusName() {
    PaymentStatus prepared = PaymentStatus.PREPARED;
    PaymentStatus sent = PaymentStatus.SENT;

    assertEquals("prepared", prepared.getStatusName());
    assertEquals("sent", sent.getStatusName());
  }
}
