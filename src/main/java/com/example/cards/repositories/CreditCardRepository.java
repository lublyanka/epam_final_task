package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import java.util.List;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Credit card repository. */
@Repository
@Hidden
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
  /**
   * Find all by account list.
   *
   * @param account the account
   * @return the list
   */
  List<CreditCard> findAllByAccount(Account account);
}
