package com.example.cards.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserAccountKeyTest {
  @Test
  public void testConstructorAndGetters() {
    User user = new User();
    Account account = new Account();
    UserAccountKey userAccountKey = new UserAccountKey(user, account);

    assertEquals(user, userAccountKey.getUserId());
    assertEquals(account, userAccountKey.getAccountId());
  }


  @Test
  void setUserId() {
    User user = new User();
    Account account = new Account();
    UserAccountKey userAccountKey = new UserAccountKey(user, account);
    userAccountKey.setUserId(user);
    assertEquals(user, userAccountKey.getUserId());
    }

  @Test
  void setAccountId() {
    User user = new User();
    Account account = new Account();
    UserAccountKey userAccountKey = new UserAccountKey(user, account);
    userAccountKey.setAccountId(account);
    assertEquals(account, userAccountKey.getAccountId());
    }

  @Test
  void defaultConstructor() {
    User user = new User();
    Account account = new Account();
    UserAccountKey userAccountKey = new UserAccountKey();
    assertNull(userAccountKey.getUserId());
    assertNull(userAccountKey.getAccountId());
    userAccountKey.setAccountId(account);
    assertEquals(account, userAccountKey.getAccountId());
    userAccountKey.setUserId(user);
    assertEquals(user, userAccountKey.getUserId());
  }
}
