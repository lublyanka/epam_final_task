package com.example.cards.controllers;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.example.cards.requests.PaymentRequest;
import com.example.cards.services.AccountService;
import com.example.cards.services.PaymentService;
import com.example.cards.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @GetMapping("/all")
    public ResponseEntity<?> loadUserPayments(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestParam(required = false, defaultValue = "number") String sortBy,
                                              @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        User user = userService.getUserByToken(token);
        List<Payment> payments = paymentRepository.findAllByUser(user);

        if (!(payments.isEmpty()))
            return ResponseEntity.ok(payments);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> loadPaymentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable UUID paymentId) {
        User user = userService.getUserByToken(token);
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
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

        User user = userService.getUserByToken(token);
        Optional<Account> senderAccount = accountService.getOptionalAccount(paymentRequest.getAccountId());

        if (senderAccount.isEmpty() || accountService.getAccountExistenceByUser(senderAccount.get(), user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Account account = senderAccount.get();

        if (paymentService.isPaymentSumPositive(paymentRequest.getAmount(), account)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(paymentService.savePayment(paymentRequest, user, account));
    }


}
