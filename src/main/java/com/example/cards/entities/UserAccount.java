package com.example.cards.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity

@Table(name = "user_accounts", schema = "public")
public class UserAccount {
    @EmbeddedId
    @Getter
    @Setter
    private UserAccountKey userAccountKey;

    public UserAccount(UserAccountKey userAccountKey) {
        this.userAccountKey = userAccountKey;
    }

    public UserAccount() {
    }
}
