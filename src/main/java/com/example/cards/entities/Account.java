package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** The Account entity table. */
@Entity
@Table(name = "accounts", schema = "public")
public class Account {

  @Column(name = "blocked", columnDefinition = "boolean default false")
  @Getter
  @Setter
  boolean isBlocked;

  @Column(name = "requested", columnDefinition = "boolean default false")
  @Getter
  @Setter
  boolean isRequested;

  @Id
  @Getter
  @Setter
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

  @Column(name = "available_balance", columnDefinition = "decimal(10,2)")
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

  /** Instantiates a new Account. */
  public Account() {}

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
  private List<Payment> payments;
}
