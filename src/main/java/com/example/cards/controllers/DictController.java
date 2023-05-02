package com.example.cards.controllers;

import com.example.cards.entities.dict.Currency;
import com.example.cards.services.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Autowired
    AccountService accountService;

    //dict
    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        return accountService.getAllCurrencies();
    }
}
