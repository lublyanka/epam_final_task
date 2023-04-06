package com.example.cards.controllers;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.*;
import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.PaymentRepository;
import com.example.cards.repositories.UserAccountRepository;
import com.example.cards.repositories.UserRepository;
import com.example.cards.requests.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/all")
    public ResponseEntity<?> loadUserPayments(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestParam(required = false, defaultValue = "number") String sortBy,
                                              @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
        List<Payment> payments= paymentRepository.findAllByUser(user);

        if (!(payments.isEmpty()))
            return ResponseEntity.ok(payments);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> loadPaymentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable UUID paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        if (paymentOptional.isPresent()) {
            Payment payment =paymentOptional.get();
            if (user.getId().equals(payment.getUser().getId()))
                return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           PaymentRequest paymentRequest) {
        if (!paymentRequest.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        Account senderAccount = accountRepository.findById(paymentRequest.getAccountId()).orElse(null);

        if (senderAccount == null ||
        !userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, senderAccount).isPresent())  {
            return ResponseEntity.badRequest().build();
        }
        // Check if the sender has enough balance to make the payment
        if (senderAccount.getCurrentBalance().compareTo(paymentRequest.getAmount()) < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Create the new payment
        Payment payment = new Payment();
        payment.setAccount(senderAccount);
        payment.setAmount(paymentRequest.getAmount());
        payment.setDescription(paymentRequest.getDescription());
        payment.setCreatedOn(Timestamp.from(Instant.now()));
        payment.setUpdatedOn(Timestamp.from(Instant.now()));
        payment.setStatus(PaymentStatus.PREPARED);
        payment.setUser(user);
        paymentRepository.save(payment);

        return ResponseEntity.ok(payment);//"User account created successfully.");
    }

}
