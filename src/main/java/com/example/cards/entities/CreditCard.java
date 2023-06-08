package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * The Credit card entity table.
 */
@Entity
@Table(name = "credit_cards", schema = "public")
public class CreditCard {

    @Id
    @Column(name = "card_number", length = 19)
    @Getter
    @Setter
    private String cardNumber;

    @Column(length = 10)
    @Getter
    @Setter
    private String cardType;

    @Column(name = "name")
    @Getter
    @Setter
    private String cardTitle;

    @Column(name = "card_holder", nullable = false)
    @Getter
    @Setter
    private String cardHolder;

/*
    @Column(name = "month", columnDefinition = "character(2)")
    @Getter
    @Setter
    private String month;

    @Column(name = "year", columnDefinition = "character(4)")
    @Getter
    @Setter
    private String year;
*/

    @Column(name = "valid_till")
    @Getter
    @Setter
    private Timestamp validTill;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "account")
    private Account account;

    @JsonInclude()
    @Transient
    @Getter
    @Setter
    private UUID accountId;

  /**
   * Gets account.
   *
   * @return the account
   */
  @JsonIgnore
  public Account getAccount() {
        return account;
    }

}
