package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "public")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "number", nullable = false)
    @Getter
    @Setter
    private String number;

  /*public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }*/

    @JsonProperty("currency")
    @Column(columnDefinition = "character(3)")
    @Getter
    @Setter
    private String currencyCode;


    @Column(name="available_balance", columnDefinition = "decimal(10,2) default 0.0")
    @Getter
    @Setter
    private BigDecimal currentBalance;


    @Column(name = "created_on")
    @Getter
    @Setter
    private Timestamp createdOn;

    @Column(name = "updated_on")
    @Getter
    @Setter
    private Timestamp updatedOn;

    @Column(name = "isblocked", columnDefinition = "boolean default false")
    @Getter
    @Setter
    boolean isBlocked;


    public UUID getId() {
        return id;
    }

   /* public Account(String currency) {
        this.currencyCode = currency;
    }*/

    public Account() {
    }
}
