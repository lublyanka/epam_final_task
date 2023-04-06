package com.example.cards.services;

import com.example.cards.entities.Account;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountService {

    public Comparator<Account> getComparator(String sortBy, String sortOrder) {
        Comparator<Account> comparator = switch (sortBy) {
            case "name" -> Comparator.comparing(Account::getName);
            case "balance" -> Comparator.comparing(Account::getCurrentBalance);
            default -> Comparator.comparing(x -> Integer.valueOf(x.getNumber()));
        };

        if (sortOrder.equals("desc")) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
