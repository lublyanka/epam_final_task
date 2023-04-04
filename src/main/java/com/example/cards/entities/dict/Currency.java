package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dict_currencies", schema = "public")
public class Currency {
    @Id
    @Column(name = "code", length = 3)
    Character code;

    @Column(name = "name", length = 50, nullable = false)
    String name;

    public Character getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
