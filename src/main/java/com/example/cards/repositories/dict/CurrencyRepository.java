package com.example.cards.repositories.dict;

import com.example.cards.entities.dict.Currency;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Currency repository. */
@Repository
@Hidden
public interface CurrencyRepository extends JpaRepository<Currency, String> {}
