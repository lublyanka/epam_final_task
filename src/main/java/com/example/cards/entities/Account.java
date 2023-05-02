package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("currency")
    @Column(columnDefinition = "character(3)")
    @Getter
    @Setter
    private String currencyCode;
    @Column(name="available_balance", columnDefinition = "decimal(10,2)")
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
    @Column(name = "isrequested", columnDefinition = "boolean default false")
    @Getter
    @Setter
    boolean isRequested;


    public Account() {
    }

   /* public Account(String currency) {
        this.currencyCode = currency;
    }*/

    public UUID getId() {
        return id;
    }
}
