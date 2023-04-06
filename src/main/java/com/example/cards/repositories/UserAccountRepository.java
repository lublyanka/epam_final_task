package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import com.example.cards.entities.UserAccount;
import com.example.cards.entities.UserAccountKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository  extends JpaRepository<UserAccount, UserAccountKey> {

    List<UserAccount> findAllByUserAccountKeyUserId(User userId);
    UserAccount findByUserAccountKeyAccountId(Account account);

    Optional<UserAccount> findByUserAccountKeyUserIdAndUserAccountKeyAccountId(User user, Account account);
}

