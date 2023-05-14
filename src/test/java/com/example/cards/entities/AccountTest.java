package com.example.cards.entities;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
class AccountTest {

    @Test
    void getNumber() {
        Account account  = new Account();
        account.setNumber("123");
        assertEquals("123",account.getNumber());
      }

    @Test
    void getCurrencyCode() {
        Account account  = new Account();
        account.setCurrencyCode("EUR");
        assertEquals("EUR",account.getCurrencyCode());
      }

    @Test
    void getCreatedOn() {
        Account account  = new Account();
        Timestamp now = Timestamp.from(Instant.now());
        account.setCreatedOn(now);
        assertEquals(now,account.getCreatedOn());
      }

    @Test
    void getUpdatedOn() {
        Account account  = new Account();
        Timestamp now = Timestamp.from(Instant.now());
        account.setUpdatedOn(now);
        assertEquals(now,account.getUpdatedOn());
      }
}