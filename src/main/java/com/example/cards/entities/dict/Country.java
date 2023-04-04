package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dict_countries", schema = "public")
public class Country {
    @Id
    @Column(name = "alpha_2", length = 2)
    Character alpha_2;

    @Column(name = "name", length = 50, nullable = false)
    String name;

    public Character getAlpha_2() {
        return alpha_2;
    }

    public String getName() {
        return name;
    }
}
