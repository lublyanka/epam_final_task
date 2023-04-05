package com.example.cards.repositories.dict;

import com.example.cards.entities.dict.Country;
import com.example.cards.entities.dict.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
