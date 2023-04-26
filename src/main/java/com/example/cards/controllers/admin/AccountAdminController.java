package com.example.cards.controllers.admin;

import com.example.cards.entities.Account;
import com.example.cards.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;
import java.util.UUID;

import static com.example.cards.enums.Responses.ACCOUNT_DOES_NOT_EXIST;


//TODO all paths
public class AccountAdminController {
    @Autowired
    AccountService accountService;

    // TODO rewrite to block request
    @PutMapping("/{accountId}/unblock")
    public ResponseEntity<?> unblockAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable UUID accountId) {
        Optional<Account> accountOptional = accountService.unblock(token, accountId);

        if (accountOptional.isPresent()) {
            return ResponseEntity.ok("Account unblocked successfully.");
        } else {
            return ACCOUNT_DOES_NOT_EXIST;
        }
    }
}
