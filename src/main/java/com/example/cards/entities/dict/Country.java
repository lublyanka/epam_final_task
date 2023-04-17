package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dict_countries", schema = "public")
public class Country {
    @Id
    @Column(name = "alpha_2", columnDefinition = "character (2)")
    String alpha_2;

    @Column(name = "name", length = 50, nullable = false)
    String name;

    public Country() {
    }

    // add a constructor that takes a String argument
    public Country(String alpha2) {
        this.alpha_2 = alpha2;
    }

    public String getAlpha_2() {
        return alpha_2;
    }

    public String getName() {
        return name;
    }

    /*// add a static factory method that takes a String argument and returns a Country object
    public static Country fromAlpha2(String alpha2) {
        return new Country(alpha2);
    }*/
}
