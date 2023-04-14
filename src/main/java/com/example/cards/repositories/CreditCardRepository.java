package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    List<CreditCard> findAllByAccount(Account account);



}
