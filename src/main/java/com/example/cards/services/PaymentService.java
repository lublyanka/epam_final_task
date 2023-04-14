package com.example.cards.services;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.PaymentRepository;
import com.example.cards.requests.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

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
    public boolean isPaymentSumPositive(BigDecimal sum, Account account) {
        return account.getCurrentBalance().compareTo(sum) < 0;
    }

}
