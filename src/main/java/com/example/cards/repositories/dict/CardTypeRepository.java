package com.example.cards.repositories.dict;

import com.example.cards.entities.dict.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, String> {
}
