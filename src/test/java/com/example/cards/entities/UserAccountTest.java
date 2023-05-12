package com.example.cards.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserAccountTest { @Test
public void testUserAccountCreation() {
    UserAccountKey userAccountKey = mock(UserAccountKey.class);

    User user = mock(User.class);
    when(user.getId()).thenReturn(1L);

    Account account = mock(Account.class);
    when(account.getId()).thenReturn(UUID.randomUUID());

    UserAccount userAccount = new UserAccount();
    userAccount.setUserAccountKey(userAccountKey);
    when(userAccountKey.getUserId()).thenReturn(user);
    when(userAccountKey.getAccountId()).thenReturn(account);

    assertEquals(userAccountKey, userAccount.getUserAccountKey());
    assertEquals(user, userAccount.getUserAccountKey().getUserId());
    assertEquals(account, userAccount.getUserAccountKey().getAccountId());
}}
