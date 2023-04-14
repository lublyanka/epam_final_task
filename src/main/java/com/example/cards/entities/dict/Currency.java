package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "dict_currencies", schema = "public")
public class Currency {
    @Id
    @Column(name = "code", columnDefinition = "character(3)")
    @Getter
    String code;

    @Column(name = "name", length = 50, nullable = false)
    @Getter
    String name;

    public Currency(String code) {
        this.code = code;
    }

    public Currency() {

    }


}
