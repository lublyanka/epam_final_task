package com.example.cards.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.cards.entities.dict.Currency;
import com.example.cards.services.AccountService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DictionaryControllerTest {
  @Mock private AccountService accountService;

  @InjectMocks private DictionaryController dictionaryController;

  @Test
  public void testGetAllCurrencies() {
    Currency currency1 = new Currency("USD", "US Dollar");
    Currency currency2 = new Currency("EUR", "Euro");
    List<Currency> currencies = Arrays.asList(currency1, currency2);
    when(accountService.getAllCurrencies()).thenReturn(currencies);

    List<Currency> result = dictionaryController.getAllCurrencies();

    assertEquals(currencies, result);
  }
}
