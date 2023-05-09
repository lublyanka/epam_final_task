package com.example.cards.services;

import static com.example.cards.enums.Responses.NOT_ENOUGH_MONEY;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.PaymentRepository;
import com.example.cards.requests.PaymentRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The Payment service. */
@Service
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class PaymentService {

  @Autowired private PaymentRepository paymentRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private UserService userService;

  /**
   * Gets payment by id.
   *
   * @param paymentId the payment id
   * @return the payment
   */
  public Optional<Payment> getById(UUID paymentId) {
    return paymentRepository.findById(paymentId);
  }

  /**
   * Gets payment.
   *
   * @param token the JWT token
   * @param paymentId the payment id
   * @return the payment
   */
  @Transactional
  public Optional<Payment> getPayment(String token, UUID paymentId) {
    User user = userService.getUserByToken(token);

    Optional<Payment> paymentOptional = getById(paymentId);

    if (paymentOptional.isPresent()) {
      Payment payment = paymentOptional.get();
      if (user.isAdmin()) {
        return paymentOptional;
      } else {
        if (user.getId().equals(payment.getUser().getId())) {
          return paymentOptional;
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Gets all user payments.
   *
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @param token the JWT token
   * @return the all user payments
   */
  @Transactional
  public Page<Payment> getAllUserPayments(
      String sortBy, String sortOrder, int page, int size, String token) {
    User user = userService.getUserByToken(token);
    Page<Payment> pageResult =
        paymentRepository.findAllByUser_Id(
            user.getId(),
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)));
    //pageResult.forEach(x -> x.setAccount(null));
    return pageResult;
    // paymentList.stream().sorted(getComparator(sortBy, sortOrder)).toList();
  }

  /**
   * Gets all account payments.
   *
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @param account the account
   * @return the all account payments
   */
  public Page<Payment> getAllAccountPayments(
      String sortBy, String sortOrder, int page, int size, Account account) {
    Page<Payment> pageResult =
        paymentRepository.findAllByAccount(
            account,
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)));
    //pageResult.forEach(x -> x.setAccount(null));
    return pageResult;
    // paymentList.stream().sorted(getComparator(sortBy, sortOrder)).toList();
  }

  /**
   * Save payment optional.
   *
   * @param paymentRequest the payment request
   * @param token the JWT token
   * @param account the account
   * @return the payment
   */
  @Transactional
  public Optional<Payment> savePayment(
      PaymentRequest paymentRequest, String token, Account account) {
    if (isPaymentSumPositive(paymentRequest.getAmount(), account)) {
      User user = userService.getUserByToken(token);
      Payment payment = new Payment();
      payment.setAccount(account);
      payment.setNumber(paymentRequest.getNumber());
      payment.setAmount(paymentRequest.getAmount());
      payment.setCurrencyCode(paymentRequest.getCurrency());
      payment.setDescription(paymentRequest.getDescription());
      payment.setCreatedOn(Timestamp.from(Instant.now()));
      payment.setUpdatedOn(Timestamp.from(Instant.now()));
      payment.setStatus(PaymentStatus.PREPARED);
      payment.setUser(user);
      payment = paymentRepository.save(payment);
      paymentRepository.flush();
      return Optional.of(payment);
    }
    return Optional.empty();
  }

  /**
   * Send payment optional.
   *
   * @param token the JWT token
   * @param paymentId the payment id
   * @return the payment
   */
  @Transactional
  public Optional<?> sendPayment(String token, UUID paymentId) {
    User user = userService.getUserByToken(token);
    Optional<Payment> paymentOptional = getById(paymentId);

    if (paymentOptional.isPresent()) {
      Payment payment = paymentOptional.get();
      if (user.getId().equals(payment.getUser().getId())) {
        Account account = payment.getAccount();
        if (isPaymentSumPositive(payment.getAmount(), account)) {
          if (payment.getStatus() == PaymentStatus.PREPARED) {
            payment.setStatus(PaymentStatus.SENT);
            payment.setUpdatedOn(Timestamp.from(Instant.now()));
            account.setCurrentBalance(account.getCurrentBalance().subtract(payment.getAmount()));
            accountRepository.save(account);
            payment = paymentRepository.save(payment);
            paymentRepository.flush();
            accountRepository.flush();
          }
          return Optional.of(payment);
        }
        return Optional.of(NOT_ENOUGH_MONEY);
      }
    }
    return Optional.empty();
  }

  /**
   * Is payment sum positive.
   *
   * @param sum the sum
   * @param account the account
   * @return the boolean
   */
  public boolean isPaymentSumPositive(BigDecimal sum, Account account) {
    return account.getCurrentBalance().compareTo(sum) > 0;
  }

  /**
   * Gets comparator.
   *
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @return the comparator
   */
  public Comparator<Payment> getComparator(String sortBy, String sortOrder) {
    Comparator<Payment> comparator =
        switch (sortBy) {
          case "date" -> Comparator.comparing(Payment::getCreatedOn);
          default -> Comparator.comparing(x -> Integer.valueOf(x.getNumber()));
        };

    if (sortOrder.equals("desc")) {
      comparator = comparator.reversed();
    }
    return comparator;
  }

  /**
   * Gets all payments.
   *
   * @param sortBy the sort by
   * @param sortOrder the sort order
   * @param page the page
   * @param size the size of page
   * @return the all payments
   */
  @PreAuthorize("hasAuthority ('ROLE_ADMIN') ")
  public Page<Payment> getAllPayments(String sortBy, String sortOrder, int page, int size) {
    return paymentRepository.findAll(
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)));
  }
}
