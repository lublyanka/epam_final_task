package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

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

}
