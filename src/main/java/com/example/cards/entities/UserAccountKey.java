package com.example.cards.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/** The User account key for User Account cross-table entity. */
@Embeddable
public class UserAccountKey implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @Getter
  @Setter
  private User userId;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id")
  @Getter
  @Setter
  private Account accountId;

  /**
   * Instantiates a new User account key.
   *
   * @param user the user
   * @param account the account
   */
  public UserAccountKey(User user, Account account) {
    this.userId = user;
    this.accountId = account;
  }

  /** Instantiates a new User account key. */
  public UserAccountKey() {}
}
