package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    List<CreditCard> findAllByAccount(Account account);



}
