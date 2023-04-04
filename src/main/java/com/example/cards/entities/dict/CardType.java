package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dict_card_types", schema = "public")
public class CardType {
    @Id
    @Column(name = "code", length = 2)
    Character code;

    @Column(name = "letter", length = 2, nullable = false)
    Character letter;

    @Column(name = "name", length = 50, nullable = false)
    String name;

    public Character getCode() {
        return code;
    }

    public Character getLetter() {
        return letter;
    }

    public String getName() {
        return name;
    }
}
