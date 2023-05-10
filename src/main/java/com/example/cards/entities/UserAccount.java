package com.example.cards.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** The User account cross-table entity. */
@SuppressWarnings("unused")
@Entity
@Table(name = "user_accounts", schema = "public")
public class UserAccount {
  @EmbeddedId @Getter @Setter private UserAccountKey userAccountKey;

  /**
   * Instantiates a new User account.
   *
   * @param userAccountKey the user account key
   */
  public UserAccount(UserAccountKey userAccountKey) {
    this.userAccountKey = userAccountKey;
  }

  /** Instantiates a new User account. */
  public UserAccount() {}
}
