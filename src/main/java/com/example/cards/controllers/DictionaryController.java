package com.example.cards.controllers;

import com.example.cards.entities.dict.Currency;
import com.example.cards.services.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The Dictionary controller. */
@RestController
@RequestMapping("/api/dict")
public class DictionaryController {

  @Autowired AccountService accountService;

  /**
   * Gets all currencies.
   *
   * @return the all currencies
   */
  @GetMapping("/currencies")
  public List<Currency> getAllCurrencies() {
    return accountService.getAllCurrencies();
  }
}
