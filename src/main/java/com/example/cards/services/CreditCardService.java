package com.example.cards.services;

import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

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