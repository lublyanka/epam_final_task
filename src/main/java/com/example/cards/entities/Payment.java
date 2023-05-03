package com.example.cards.entities;

import com.example.cards.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "payments", schema = "public")
public class Payment {

  @Id
  @Getter
  @Setter
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private PaymentStatus status;

  @Getter
  @Setter
  @Column(name = "amount", columnDefinition = "decimal(10,2)", nullable = false)
  private BigDecimal amount;

  @Getter
  @Setter
  @Column(name = "description")
  private String description;

  @JsonProperty("currency")
  @Column(columnDefinition = "character(3)", nullable = false)
  @Getter
  @Setter
  private String currencyCode;

  @Column(name = "number", nullable = false)
  @Getter
  @Setter
  private String number;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @Getter
  @Setter
  @JsonIgnore
  private User user;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @Column(name = "created_on", nullable = false)
  @CreationTimestamp
  @Getter
  @Setter
  private Timestamp createdOn;

  @Column(name = "updated_on")
  @CreationTimestamp
  @Getter
  @Setter
  private Timestamp updatedOn;
}
