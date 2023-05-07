package com.example.cards.controllers.billing;

import static com.example.cards.enums.Responses.*;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.requests.PaymentRequest;
import com.example.cards.services.AccountService;
import com.example.cards.services.PaymentService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

  @Autowired private PaymentService paymentService;
  @Autowired private AccountService accountService;

  @GetMapping("/all")
  public ResponseEntity<?> loadUserPayments(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false, defaultValue = "number") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Page<Payment> payments = paymentService.getAllUserPayments(sortBy, sortOrder, page, size, token);

    if (!(payments.isEmpty())) return ResponseEntity.ok(payments);
    else return ResponseEntity.noContent().build();
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<?> loadPaymentById(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID paymentId) {

    Optional<Payment> paymentOptional = paymentService.getPayment(token, paymentId);
    if (paymentOptional.isPresent()) {
      return ResponseEntity.ok(paymentOptional.get());
    }
    return PAYMENT_NOT_FOUND;
  }

  @PutMapping("/{paymentId}/send")
  public ResponseEntity<?> sendPayment(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID paymentId) {

    Optional<?> paymentOptional = paymentService.sendPayment(token, paymentId);
    if (paymentOptional.isPresent()) {
      if (paymentOptional.get() instanceof Payment) {
        return ResponseEntity.ok(paymentOptional.get());
      }
      else return (ResponseEntity<?>) paymentOptional.get();
    }
    return PAYMENT_NOT_FOUND;
  }

  @PutMapping("/create")
  public ResponseEntity<?> createPayment(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PaymentRequest paymentRequest) {

    if (!paymentRequest.isValid()) {
      return ResponseEntity.badRequest().build();
    }

    Optional<Account> senderAccount =
        accountService.getOptionalAccount(token, paymentRequest.getAccountId());

    if (senderAccount.isEmpty()) {
      return ACCOUNT_DOES_NOT_EXIST;
    }
    Optional<Payment> payment = paymentService.savePayment(paymentRequest, token, senderAccount.get());

    if (payment.isEmpty()) {
      return NOT_ENOUGH_MONEY;
    }
    return ResponseEntity.ok(payment.get());
  }
}