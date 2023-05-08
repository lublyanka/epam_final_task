package com.example.cards.repositories.dict;

import com.example.cards.entities.dict.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Currency repository. */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {}
