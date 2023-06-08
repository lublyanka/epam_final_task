package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** The interface Payment repository. */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Repository
@Hidden
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  /**
   * Find all by user id page.
   *
   * @param userId the user id
   * @param pageable the pageable
   * @return the page
   */
  Page<Payment> findAllByUser_Id(@Param("userId")Long userId, Pageable pageable);
  /**
   * Find all by account page.
   *
   * @param account the account
   * @param pageable the pageable
   * @return the page
   */
  Page<Payment> findAllByAccount(Account account, Pageable pageable);
}
