package com.example.cards.services;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.PaymentRepository;
import com.example.cards.requests.PaymentRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired private PaymentRepository paymentRepository;
  @Autowired private UserService userService;

  public Optional<Payment> getById(UUID paymentId) {
    return paymentRepository.findById(paymentId);
  }

  public Optional<Payment> getPayment(String token, UUID paymentId) {
    User user = userService.getUserByToken(token);
    Optional<Payment> paymentOptional = getById(paymentId);

    if (paymentOptional.isPresent()) {
      Payment payment = paymentOptional.get();
      if (user.getId().equals(payment.getUser().getId())) return paymentOptional;
    }
    return Optional.empty();
  }

  public List<Payment> getPaymentListByUser(String sortBy, String sortOrder, String token) {
    User user = userService.getUserByToken(token);
    List<Payment> paymentList = paymentRepository.findAllByUser(user);
    return paymentList.stream().sorted(getComparator(sortBy, sortOrder)).toList();
  }

  public Optional<Payment> savePayment(
      PaymentRequest paymentRequest, String token, Account account) {
    if (isPaymentSumPositive(paymentRequest.getAmount(), account)) {
      User user = userService.getUserByToken(token);
      Payment payment = new Payment();
      payment.setAccount(account);
      payment.setAmount(paymentRequest.getAmount());
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

  public Optional<Payment> sendPayment(String token, UUID paymentId) {
    User user = userService.getUserByToken(token);
    Optional<Payment> paymentOptional = getById(paymentId);

    if (paymentOptional.isPresent()) {
      Payment payment = paymentOptional.get();
      if (user.getId().equals(payment.getUser().getId())) {
        if (payment.getStatus() == PaymentStatus.PREPARED) {
          payment.setStatus(PaymentStatus.SENT);
          payment.setUpdatedOn(Timestamp.from(Instant.now()));
          payment = paymentRepository.save(payment);
          paymentRepository.flush();
        }
        return Optional.of(payment);
      }
    }
    return Optional.empty();
  }


  public boolean isPaymentSumPositive(BigDecimal sum, Account account) {
    return account.getCurrentBalance().compareTo(sum) < 0;
  }

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
}
