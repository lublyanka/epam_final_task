package com.example.cards.entities.dict;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

  private Currency currency;

  @BeforeEach
  void setUp() {
    currency = new Currency("EUR", "Euro");
  }

  @Test
  void getCode() {
    assertEquals("EUR", currency.getCode());
  }

  @Test
  void getName() {
    assertEquals("Euro", currency.getName());
  }

  @Test
  void defaultConstructor() {
    Currency currency = new Currency();
    assertNull(currency.getCode());
    assertNull(currency.getName());
  }

  @Test
  void codeConstructor() {
    Currency currency = new Currency("EUR");
    assertEquals("EUR", currency.getCode());
    assertNull(currency.getName());
  }
}
