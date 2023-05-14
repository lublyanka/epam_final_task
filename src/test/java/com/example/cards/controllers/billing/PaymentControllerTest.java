package com.example.cards.controllers.billing;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.requests.PaymentRequest;
import com.example.cards.services.AccountService;
import com.example.cards.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.cards.enums.Responses.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Usage of doReturn because of wildcard <a
 * href="http://not4j.com/mocking-method-with-generic-return-type/">...</a>
 */
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
  @Mock private PaymentService paymentService;
  @Mock private AccountService accountService;

  @InjectMocks private PaymentController paymentController;

  @Test
  public void testLoadUserPayments_NonEmptyPayments_ReturnsOkResponse() {
    String token = "Valid token";

    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Payment> mockPayments = new ArrayList<>();
    Payment payment1 = new Payment();
    payment1.setNumber("P001");
    Payment payment2 = new Payment();
    payment2.setNumber("P002");
    mockPayments.add(payment1);
    mockPayments.add(payment2);
    Page<Payment> mockPage = new PageImpl<>(mockPayments);

    ResponseEntity<?> expectedResponse = ResponseEntity.ok(mockPage);
    when(paymentService.getAllUserPayments(sortBy, sortOrder, page, size, token))
        .thenReturn(mockPage);

    ResponseEntity<?> response =
        paymentController.loadUserPayments(token, sortBy, sortOrder, page, size);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testLoadUserPayments_EmptyPayments_ReturnsNoContent() {
    String token = "Valid token";

    String sortBy = "number";
    String sortOrder = "asc";
    int page = 0;
    int size = 10;

    List<Payment> mockPayments = new ArrayList<>();
    Page<Payment> mockPage = new PageImpl<>(mockPayments);

    ResponseEntity<?> expectedResponse = ResponseEntity.noContent().build();
    when(paymentService.getAllUserPayments(sortBy, sortOrder, page, size, token))
        .thenReturn(mockPage);

    ResponseEntity<?> response =
        paymentController.loadUserPayments(token, sortBy, sortOrder, page, size);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testLoadPaymentById_ExistingPayment_ReturnsOkResponse() {
    String token = "mock-token";
    UUID paymentId = UUID.randomUUID();

    Payment mockPayment = new Payment();
    mockPayment.setId(paymentId);
    mockPayment.setNumber("P001");

    ResponseEntity<?> expectedResponse = ResponseEntity.ok(mockPayment);

    when(paymentService.getPayment(token, paymentId)).thenReturn(Optional.of(mockPayment));

    ResponseEntity<?> response = paymentController.loadPaymentById(token, paymentId);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testLoadPaymentById_NonExistingPayment_ReturnsNotFoundResponse() {
    String token = "mock-token";
    UUID paymentId = UUID.randomUUID();

    when(paymentService.getPayment(token, paymentId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = paymentController.loadPaymentById(token, paymentId);

    assertEquals(PAYMENT_NOT_FOUND, response);
  }

  @Test
  public void testSendPayment_ValidPayment_ReturnsOkResponse() {
    String token = "mock-token";
    UUID paymentId = UUID.randomUUID();

    Payment mockPayment = new Payment();
    mockPayment.setId(paymentId);
    mockPayment.setNumber("P001");

    doReturn(Optional.of(mockPayment)).when(paymentService).sendPayment(token, paymentId);

    ResponseEntity<?> response = paymentController.sendPayment(token, paymentId);

    assertEquals(ResponseEntity.ok(mockPayment), response);
  }

  @Test
  public void testSendPayment_InvalidPayment_ReturnsErrorResponse() {

    String token = "mock-token";
    UUID paymentId = UUID.randomUUID();
    ResponseEntity<?> errorResponse = NOT_ENOUGH_MONEY;
    doReturn(Optional.of(errorResponse)).when(paymentService).sendPayment(token, paymentId);

    ResponseEntity<?> response = paymentController.sendPayment(token, paymentId);

    assertEquals(errorResponse, response);
  }

  @Test
  public void testSendPayment_NonExistingPayment_ReturnsNotFoundResponse() {
    String token = "mock-token";
    UUID paymentId = UUID.randomUUID();
    when(paymentService.sendPayment(token, paymentId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = paymentController.sendPayment(token, paymentId);

    assertEquals(PAYMENT_NOT_FOUND, response);
  }

  @Test
  public void testCreatePayment_ValidPaymentRequest_ReturnsOkResponse() {
    String token = "mock-token";

    PaymentRequest mockPaymentRequest = new PaymentRequest();
    mockPaymentRequest.setAccountId(UUID.randomUUID());
    mockPaymentRequest.setAmount(BigDecimal.valueOf(100.00));
    mockPaymentRequest.setDescription("Mock payment");
    mockPaymentRequest.setNumber("123");

    Account mockSenderAccount = new Account();
    mockSenderAccount.setId(UUID.randomUUID());
    mockSenderAccount.setNumber("A001");
    mockSenderAccount.setCurrentBalance(BigDecimal.valueOf(500.00));

    Payment mockPayment = new Payment();
    mockPayment.setId(UUID.randomUUID());
    mockPayment.setNumber("P001");

    ResponseEntity<?> expectedResponse = ResponseEntity.ok(mockPayment);

    when(accountService.getOptionalAccount(token, mockPaymentRequest.getAccountId()))
        .thenReturn(Optional.of(mockSenderAccount));
    when(paymentService.savePayment(mockPaymentRequest, token, mockSenderAccount))
        .thenReturn(Optional.of(mockPayment));

    ResponseEntity<?> response = paymentController.createPayment(token, mockPaymentRequest);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testCreatePayment_InvalidPaymentRequest_ReturnsBadRequestResponse() {
    String token = "mock-token";

    PaymentRequest invalidPaymentRequest = new PaymentRequest();
    ResponseEntity<?> expectedResponse = ResponseEntity.badRequest().build();

    ResponseEntity<?> response = paymentController.createPayment(token, invalidPaymentRequest);

    assertEquals(expectedResponse, response);
  }

  @Test
  public void testCreatePayment_NonExistingSenderAccount_ReturnsAccountDoesNotExistResponse() {
    String token = "mock-token";

    PaymentRequest mockPaymentRequest = new PaymentRequest();
    mockPaymentRequest.setAccountId(UUID.randomUUID());
    mockPaymentRequest.setAmount(BigDecimal.valueOf(100.00));
    mockPaymentRequest.setDescription("Mock payment");
    mockPaymentRequest.setNumber("123");

    when(accountService.getOptionalAccount(token, mockPaymentRequest.getAccountId()))
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = paymentController.createPayment(token, mockPaymentRequest);

    assertEquals(ACCOUNT_DOES_NOT_EXIST, response);
  }

  @Test
  public void testCreatePayment_ZeroAmountOnAccount_ReturnsNotEnoughMoney() {
    String token = "mock-token";
    Account mockSenderAccount = new Account();
    mockSenderAccount.setId(UUID.randomUUID());
    mockSenderAccount.setNumber("A001");
    mockSenderAccount.setCurrentBalance(BigDecimal.valueOf(0.00));

    PaymentRequest mockPaymentRequest = new PaymentRequest();
    mockPaymentRequest.setAccountId(UUID.randomUUID());
    mockPaymentRequest.setAmount(BigDecimal.valueOf(100.00));
    mockPaymentRequest.setDescription("Mock payment");
    mockPaymentRequest.setNumber("123");

    when(accountService.getOptionalAccount(token, mockPaymentRequest.getAccountId()))
        .thenReturn(Optional.of(mockSenderAccount));
    when(paymentService.savePayment(mockPaymentRequest, token, mockSenderAccount))
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = paymentController.createPayment(token, mockPaymentRequest);

    assertEquals(NOT_ENOUGH_MONEY, response);
  }
}
