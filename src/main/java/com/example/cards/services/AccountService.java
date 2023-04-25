package com.example.cards.services;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import com.example.cards.entities.UserAccount;
import com.example.cards.entities.UserAccountKey;
import com.example.cards.repositories.AccountRepository;
import com.example.cards.repositories.UserAccountRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired private AccountRepository accountRepository;

  @Autowired private UserAccountRepository userAccountRepository;

  @Autowired private UserService userService;

  @Autowired private CurrencyRepository currencyRepository;

  public Optional<Account> getOptionalAccount(String token, UUID accountId) {
    User user = userService.getUserByToken(token);
    Optional<Account> accountOptional = accountRepository.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<UserAccount> userAccountByUser =
          userAccountRepository.findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);
      if (userAccountByUser.isPresent()) {
        return accountOptional;
      }
    }
    return Optional.empty();
  }

  public Page<Account>getAllUserAccounts(
      String sortBy, String sortOrder, int page, int size, String token) {
    User user = userService.getUserByToken(token);
    Page<Account> pageResult =
        accountRepository.findByUserIdWithPagination(
            user,
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)));
    return pageResult;
  }

  public Optional<Account> refillAccount(String token, UUID accountId, BigDecimal amount) {
    Optional<Account> accountOptional = getOptionalAccount(token, accountId);
    if (accountOptional.isEmpty()) return Optional.empty();

    if (amount.compareTo(BigDecimal.ZERO) != 1) {
      return accountOptional;
    }
    Account account = accountOptional.get();
    account.setCurrentBalance(account.getCurrentBalance().add(amount));
    return Optional.of(accountRepository.save(account));
  }

  public Account saveAccount(Account account, String token) {
    User user = userService.getUserByToken(token);
    Account accountToSave = account;
    accountToSave.setCurrentBalance(BigDecimal.ZERO);
    accountToSave.setCreatedOn(Timestamp.from(Instant.now()));
    accountToSave.setUpdatedOn(Timestamp.from(Instant.now()));
    accountToSave = accountRepository.save(account);
    UserAccountKey userAccountKey = new UserAccountKey(user, accountToSave);
    userAccountRepository.save(new UserAccount(userAccountKey));
    accountRepository.flush();
    userAccountRepository.flush();
    return accountToSave;
  }

  public Optional<Account> block(String token, UUID accountId) {
    Optional<Account> accountOptional = getOptionalAccount(token, accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (!account.isBlocked()) {
        account.setBlocked(true);
        account.setUpdatedOn(Timestamp.from(Instant.now()));
        accountRepository.save(account);
      }
      return accountOptional;
    } else return Optional.empty();
  }

  public Optional<Account> unblock(String token, UUID accountId) {
    Optional<Account> accountOptional = getOptionalAccount(token, accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (account.isBlocked()) {
        account.setBlocked(false);
        account.setUpdatedOn(Timestamp.from(Instant.now()));
        accountRepository.save(account);
      }
      return accountOptional;
    } else return Optional.empty();
  }

  public Optional<Account> unblockRequest(String token, UUID accountId) {
    Optional<Account> accountOptional = getOptionalAccount(token, accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (account.isBlocked()&&!account.isRequested()) {
        account.setRequested(true);
        accountRepository.save(account);
      }
      return accountOptional;
    } else return Optional.empty();
  }

  public boolean isAccountCurrencyPresent(Account account) {
    return currencyRepository.existsById(account.getCurrencyCode());
  }

  public Comparator<Account> getComparator(String sortBy, String sortOrder) {
    Comparator<Account> comparator =
        switch (sortBy) {
          case "name" -> Comparator.comparing(Account::getName);
          case "balance" -> Comparator.comparing(Account::getCurrentBalance);
          default -> Comparator.comparing(x -> Integer.valueOf(x.getNumber()));
        };

    if (sortOrder.equals("desc")) {
      comparator = comparator.reversed();
    }
    return comparator;
  }
}
