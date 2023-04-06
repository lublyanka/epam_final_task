package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@Entity
@Table(name = "credit_cards", schema = "public")
public class CreditCard {

    @Id
    @Column(name = "card_number", length = 19)
    @Getter
    @Setter
    private String cardNumber;

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

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "account")
    private Account account;

    @JsonInclude()
    @Transient
    @Getter
    @Setter
    private UUID accountId;

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    /* public UUID getAccountId() {
        return account.getId();
    }*/

    /*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type")
    @JsonDeserialize(using = CardTypeDeserializer.class)
    private CardType cardType;*/

    /*    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    private Currency currency;*/

    /*    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }*/

}
