package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springfox.documentation.annotations.ApiIgnore;

/** The interface Account repository. */
@Hidden
public interface AccountRepository extends JpaRepository<Account, UUID> {

  /**
   * Find all by user id with pagination page.
   *
   * @param user the user
   * @param pageable the pageable
   * @return the page
   */
  @Query(
      "SELECT a FROM Account a LEFT JOIN FETCH UserAccount ua on a = ua.userAccountKey.accountId WHERE ua.userAccountKey.userId = :user")
  Page<Account> findAllByUserIdWithPagination(@Param("user") User user, Pageable pageable);

  /**
   * Find all by user id with pagination and status page.
   *
   * @param user the user
   * @param isBlocked whether the user is blocked
   * @param pageable the pageable
   * @return the page
   */
  @Query(
      "SELECT a FROM Account a LEFT JOIN FETCH UserAccount ua on a = ua.userAccountKey.accountId WHERE ua.userAccountKey.userId = :user AND a.isBlocked = :isBlocked")
  Page<Account> findAllByUserIdWithPaginationAndStatus(
      @Param("user") User user, boolean isBlocked, Pageable pageable);
}
