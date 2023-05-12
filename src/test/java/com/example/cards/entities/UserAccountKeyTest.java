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

  /*@Test
  public void testDefaultConstructor() {
    UserAccountKey userAccountKey = new UserAccountKey();

    assertNotNull(userAccountKey.getUserId());
    assertNotNull(userAccountKey.getAccountId());
  }*/
    }
