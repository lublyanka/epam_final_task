package com.example.cards.controllers.acc;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import com.example.cards.entities.UserAccount;
import com.example.cards.entities.UserAccountKey;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.UserAccountRepository;
import com.example.cards.repositories.UserRepository;
import com.example.cards.repositories.dict.CardTypeRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    public ResponseEntity<?> loadUserAccounts(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        List<UserAccount> userAccountList = userAccountRepository
                .findAllByUserAccountKeyUserId(user);
        List<Account> accounts = userAccountList
                .stream()
                .map(userAccount -> userAccount.getUserAccountKey().getAccountId())
                /* .map(UserAccount::getUserAccountKey).
                .map(UserAccountKey::getAccountId)
                .map(accountRepository::findById)*/
                .toList();

        return ResponseEntity.ok(accounts);

    }

    @PostMapping("/create")
    public ResponseEntity<?> createUserAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Account account) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));
        if (!cardTypeRepository.existsById(account.getCardType()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Type does not exist.");
        if (!currencyRepository.existsById(account.getCurrencyCode()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency code does not exist.");

        /*Currency currency = account.getCurrency();
        account.setCurrency(currency);*/
        Account accountToSave = account;
        accountToSave.setCreatedOn(Timestamp.from(Instant.now()));
        accountToSave.setUpdatedOn(Timestamp.from(Instant.now()));
        accountToSave = accountRepository.save(account);
        UserAccountKey userAccountKey = new UserAccountKey(user, accountToSave);
        UserAccount userAccount = new UserAccount(userAccountKey);
        userAccount = userAccountRepository.save(userAccount);
        accountRepository.flush();
        userAccountRepository.flush();
        return ResponseEntity.ok(accountToSave);//"User account created successfully.");
    }

    @PutMapping("/{accountId}/block")
    public ResponseEntity<?> blockAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @PathVariable UUID accountId) {
        String email = jwtTokenUtil.extractUsername(token);
        User user = userRepository.findByEmail(email);
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Optional<UserAccount> userAccountOptional = userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);

            if (userAccountOptional.isPresent()) {
                UserAccount userAccount = userAccountOptional.get();
                if (!account.isBlocked()) {
                    account.setBlocked(true);
                    account.setUpdatedOn(Timestamp.from(Instant.now()));
                    accountRepository.save(account);
                }
                return ResponseEntity.ok("Account blocked successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{accountId}/unblock")
    public ResponseEntity<?> unblockAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @PathVariable UUID accountId) {
        String email = jwtTokenUtil.extractUsername(token);
        User user = userRepository.findByEmail(email);

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Optional<UserAccount> userAccountOptional = userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);

            if (userAccountOptional.isPresent()) {
                UserAccount userAccount = userAccountOptional.get();
                if (account.isBlocked()) {
                    account.setBlocked(false);
                    account.setUpdatedOn(Timestamp.from(Instant.now()));
                    accountRepository.save(account);
                }
                return ResponseEntity.ok("Account unblocked successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
