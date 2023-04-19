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

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(PaymentRequest paymentRequest, User user, Account account) {
        // Create the new payment
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
        return payment;
    }

    public Payment sendPayment(Payment payment) {
        payment.setStatus(PaymentStatus.SENT);
        payment.setUpdatedOn(Timestamp.from(Instant.now()));
        payment = paymentRepository.save(payment);
        paymentRepository.flush();
        return payment;
    }

    public boolean isPaymentSumPositive(BigDecimal sum, Account account) {
        return account.getCurrentBalance().compareTo(sum) < 0;
    }

    public Optional<Payment> getById(UUID paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public List<Payment> getPaymentListByUser(String sortBy, String sortOrder,User user) {
        List<Payment> paymentList = paymentRepository.findAllByUser(user);
        return paymentList.stream()
                .sorted(getComparator(sortBy, sortOrder))
                .toList();
    }

    public Comparator<Payment> getComparator(String sortBy, String sortOrder) {
        Comparator<Payment> comparator = switch (sortBy) {
            case "date" -> Comparator.comparing(Payment::getCreatedOn);
            default -> Comparator.comparing(x -> Integer.valueOf(x.getNumber()));
        };

        if (sortOrder.equals("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
