package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "public")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "card_number", length = 19)
    @Getter
    @Setter
    private String cardNumber;

    /*    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "card_type")
        @JsonDeserialize(using = CardTypeDeserializer.class)
        private CardType cardType;*/
    @Column(columnDefinition = "character(2)")
    @Getter
    @Setter
    private String cardType;

    @Column(name = "month", columnDefinition = "character(2)")
    @Getter
    @Setter
    private String month;

    @Column(name = "year", columnDefinition = "character(4)")
    @Getter
    @Setter
    private String year;

/*    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    private Currency currency;*/

    @JsonProperty("currency")
    @Column(columnDefinition = "character(3)")
    @Getter
    @Setter
    private String currencyCode;

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

/*    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }*/

    /*public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }*/

    public Account(String cardNumber, String cardType, String month, String year, String currency) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.month = month;
        this.year = year;
        this.currencyCode = currency;
        //this.currency = currency;
    }

    public Account() {
    }
}
