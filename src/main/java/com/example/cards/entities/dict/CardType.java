package com.example.cards.entities.dict;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dict_card_types", schema = "public")
public class CardType {
    @Id
    @Column(name = "code", columnDefinition = "character(2)")
    @Getter
    String code;

    @JsonIgnore
    @Column(name = "letter",  columnDefinition = "character(2) not null")
    @Getter
    String letter;

    @JsonIgnore
    @Column(name = "name", length = 50, nullable = false)
    @Getter
    String name;

    public CardType(String code) {
        this.code = code;
    }
}
