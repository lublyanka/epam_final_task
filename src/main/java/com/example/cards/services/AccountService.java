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
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyRepository currencyRepository;


    public Optional<Account> getOptionalAccount(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    public Optional<Account> getOptionalAccount(String token, UUID accountId) {
        User user = userService.getUserByToken(token);
        Optional<Account> accountOptional = getOptionalAccount(accountId);
        if (accountOptional.isPresent())
            accountOptional = getAccountExistenceByUser(accountOptional.get(), user);
        return accountOptional;
    }

    public Optional<Account> getAccountExistenceByUser(Account account, User user) {
        Optional<UserAccount> userAccountByUser = getUserAccount(user, account);
        if (userAccountByUser.isPresent())
            return Optional.of(account);
        else
            return Optional.empty();
    }


    public Account getSavedAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<UserAccount> getUserAccount(User user, Account account) {
        return userAccountRepository
                .findByUserAccountKeyUserIdAndUserAccountKeyAccountId(user, account);
    }

    public List<Account> getAllUserAccounts(String sortBy, String sortOrder, User user) {
        List<UserAccount> userAccountList = getAllUserAccountsByUserAccountKeyUserId(user);
        return userAccountList
                .stream()
                .map(userAccount -> userAccount.getUserAccountKey().getAccountId().getId())
                .map(this::getOptionalAccount)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(getComparator(sortBy, sortOrder))
                .toList();
    }

    public List<UserAccount> getAllUserAccountsByUserAccountKeyUserId(User user) {
        return userAccountRepository
                .findAllByUserAccountKeyUserId(user);
    }

    public void refillAccount(BigDecimal amount, Account account) {
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountRepository.save(account);
    }

    public Account saveAccount(Account account, User user) {
        Account accountToSave = account;
        accountToSave.setCreatedOn(Timestamp.from(Instant.now()));
        accountToSave.setUpdatedOn(Timestamp.from(Instant.now()));
        accountToSave = accountRepository.save(account);
        UserAccountKey userAccountKey = new UserAccountKey(user, accountToSave);
        userAccountRepository.save(new UserAccount(userAccountKey));
        accountRepository.flush();
        userAccountRepository.flush();
        return accountToSave;
    }

    public void block(Account account) {
        if (!account.isBlocked()) {
            account.setBlocked(true);
            account.setUpdatedOn(Timestamp.from(Instant.now()));
            accountRepository.save(account);
        }
    }

    public void unblock(Account account) {
        if (account.isBlocked()) {
            account.setBlocked(false);
            account.setUpdatedOn(Timestamp.from(Instant.now()));
            accountRepository.save(account);
        }
    }

    public boolean isAccountCurrencPresent(Account account) {
        return currencyRepository.existsById(account.getCurrencyCode());
    }


    public Comparator<Account> getComparator(String sortBy, String sortOrder) {
        Comparator<Account> comparator = switch (sortBy) {
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
