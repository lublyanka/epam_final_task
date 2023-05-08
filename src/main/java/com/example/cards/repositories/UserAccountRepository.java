package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import com.example.cards.entities.UserAccount;
import com.example.cards.entities.UserAccountKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** The interface User account repository. */
public interface UserAccountRepository extends JpaRepository<UserAccount, UserAccountKey> {

  /**
   * Find all by user account key user id list.
   *
   * @param userId the user id
   * @return the list
   */
  List<UserAccount> findAllByUserAccountKeyUserId(User userId);
  /**
   * Find by user account key account id user account.
   *
   * @param account the account
   * @return the user account
   */
  UserAccount findByUserAccountKeyAccountId(Account account);

  /**
   * Find by user account key user id and user account key account id optional.
   *
   * @param user the user
   * @param account the account
   * @return the optional
   */
  Optional<UserAccount> findByUserAccountKeyUserIdAndUserAccountKeyAccountId(
      User user, Account account);
}
