package com.example.cards.controllers;

import com.example.cards.JwtTokenUtil;
import com.example.cards.entities.*;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.CreditCardRepository;
import com.example.cards.repositories.UserAccountRepository;
import com.example.cards.repositories.UserRepository;
import com.example.cards.repositories.dict.CardTypeRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import com.example.cards.services.AccountService;
import com.example.cards.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private AccountService accountService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    public ResponseEntity<?> loadUserAccounts(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestParam(required = false, defaultValue = "number") String sortBy,
                                              @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        List<UserAccount> userAccountList = userAccountRepository
                .findAllByUserAccountKeyUserId(user);
        List<Account> accounts = userAccountList
                .stream()
                .map(userAccount -> userAccount.getUserAccountKey().getAccountId().getId())
                .map(accountRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(accountService.getComparator(sortBy, sortOrder))
                .toList();

        if (!(accounts.isEmpty()))
            return ResponseEntity.ok(accounts);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> loadAccountById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable UUID accountId) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account).isPresent())
                return ResponseEntity.ok(account);
        }

      /*  Optional<Account> accounts = userAccountList
                .stream()
                .map(userAccount -> userAccount.getUserAccountKey().getAccountId()).findFirst();
      */
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{accountId}/block")
    public ResponseEntity<?> blockAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @PathVariable UUID accountId) {
        String email = jwtTokenUtil.extractUsername(token);
        User user = userRepository.findByEmail(email);
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Optional<UserAccount> userAccountOptional = userAccountRepository
                    .findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);

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
            Optional<UserAccount> userAccountOptional = userAccountRepository
                    .findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);

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

    @GetMapping("/{accountId}/cards")
    public ResponseEntity<?> loadAccountCreditCards(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                    @PathVariable UUID accountId) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account).isPresent()) {
                List<CreditCard> creditCards = creditCardRepository.findAllByAccount(account);
                //here should be another way of doing it. maybe initialize it in constructor??
                creditCards.forEach(card -> card.setAccountId(account.getId()));
                return ResponseEntity.ok(creditCards);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{accountId}/{cardNumber}")
    public ResponseEntity<?> loadCreditCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @PathVariable UUID accountId,
                                            @PathVariable String cardNumber) {

        if (!creditCardService.isValidCreditCardNumber(cardNumber))
            return ResponseEntity.badRequest().body("Invalid credit card number");

        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account).isPresent()) {
                Optional<CreditCard> creditCardOptional = creditCardRepository.findById(cardNumber);
                if (creditCardOptional.isPresent()) {
                    CreditCard creditCard = creditCardOptional.get();
                    if (account.getId().equals(creditCard.getAccount().getId())) {
                        creditCard.setAccountId(account.getId());
                        return ResponseEntity.ok(creditCard);
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{accountId}/refill")
    public ResponseEntity<?> replenishAccount(@PathVariable UUID accountId, @RequestParam BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountRepository.save(account);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUserAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody Account account) {
        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        if (!currencyRepository.existsById(account.getCurrencyCode()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency code does not exist.");

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

    @PostMapping("/addCard")
    public ResponseEntity<?> addCredicCard(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @RequestBody CreditCard creditCard) {

        if (!creditCardService.isValidCreditCardNumber(creditCard.getCardNumber()))
            return ResponseEntity.badRequest().body("Invalid credit card number");

        User user = userRepository.findByEmail(jwtTokenUtil.extractUsername(token));

        if (!cardTypeRepository.existsById(creditCard.getCardType()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Type does not exist.");

        Optional<Account> account = accountRepository.findById(creditCard.getAccountId());
        if (account.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account does not exist.");
        creditCard.setAccount(account.get());
        creditCardRepository.save(creditCard);
        creditCardRepository.flush();
        return ResponseEntity.ok(creditCard);
    }


}
