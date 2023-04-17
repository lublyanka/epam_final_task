package com.example.cards.services;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.repositories.CreditCardRepository;
import com.example.cards.repositories.dict.CardTypeRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;


    public List<CreditCard> getCreditCards(Account account) {
        List<CreditCard> creditCards = creditCardRepository.findAllByAccount(account);
        //TODO
        //here should be another way of doing it. maybe initialize it in constructor??
        creditCards.forEach(card -> card.setAccountId(account.getId()));
        return creditCards;
    }

    public Optional<CreditCard> getCreditCard(String cardNumber, Account account) {
        Optional<CreditCard> creditCardOptional = creditCardRepository.findById(cardNumber);
        if (creditCardOptional.isPresent()) {
            CreditCard creditCard = creditCardOptional.get();
            if (account.getId().equals(creditCard.getAccount().getId())) {
                //TODO
                creditCard.setAccountId(account.getId());
                return Optional.of(creditCard);
            }
        }
        return Optional.empty();
    }

    public CreditCard saveCreditCard(CreditCard creditCard, Account account) {
        creditCard.setAccount(account);
        CreditCard creditCardSaved = creditCardRepository.save(creditCard);
        creditCardRepository.flush();
        return creditCardSaved;
    }

    public boolean isCreditCardTypePresent(CreditCard creditCard) {
        return cardTypeRepository.existsById(creditCard.getCardType());
    }

    public boolean isValidCreditCardNumber(String creditCardNumber) {
        // Strip any whitespace or dashes from the credit card number
        String strippedCreditCardNumber = creditCardNumber.replaceAll("[\\s-]+", "");

        // Check that the stripped credit card number only contains digits
        if (!strippedCreditCardNumber.matches("\\d+")) {
            return false;
        }

        // Check that the stripped credit card number is of valid length for different card types
        if (strippedCreditCardNumber.length() < 13 || strippedCreditCardNumber.length() > 19) {
            return false;
        }

        // Apply the Luhn algorithm to the stripped credit card number
        int sum = 0;
        boolean alternate = false;
        for (int i = strippedCreditCardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(strippedCreditCardNumber.substring(i, i + 1));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}