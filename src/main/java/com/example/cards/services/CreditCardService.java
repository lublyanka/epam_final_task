package com.example.cards.services;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.repositories.CreditCardRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** The Credit card service. */
@Service
@RequiredArgsConstructor
public class CreditCardService {

  private final CreditCardRepository creditCardRepository;

  /**
   * Gets credit cards.
   *
   * @param account the account
   * @return the credit cards
   */
  public List<CreditCard> getCreditCards(Account account) {
    List<CreditCard> creditCards = creditCardRepository.findAllByAccount(account);
    // TODO here should be another way of doing it. maybe initialize it in constructor??
    creditCards.forEach(
        card -> {
          card.setAccountId(account.getId());
          String cardNumber = card.getCardNumber();
          card.setCardNumber(cardNumber.substring(cardNumber.length() - 4));
        });
    return creditCards;
  }

  /**
   * Gets credit card.
   *
   * @param cardNumber the card number
   * @param account the account
   * @return the credit card
   */
  public Optional<CreditCard> getCreditCard(String cardNumber, Account account) {
    Optional<CreditCard> creditCardOptional = creditCardRepository.findById(cardNumber);
    if (creditCardOptional.isPresent()) {
      CreditCard creditCard = creditCardOptional.get();
      if (account.getId().equals(creditCard.getAccount().getId())) {
        // TODO this is strange to do here, but it works
        creditCard.setAccountId(account.getId());
        return Optional.of(creditCard);
      }
    }
    return Optional.empty();
  }

  /**
   * Save credit card.
   *
   * @param creditCard the credit card
   * @param account the account
   * @return the credit card
   */
  public CreditCard saveCreditCard(CreditCard creditCard, Account account) {
    creditCard.setAccount(account);
    creditCard.setCardHolder(creditCard.getCardHolder().strip());
    creditCard.setCardType(creditCard.getCardType().strip());
    creditCard.setCardTitle(creditCard.getCardTitle().strip());
    creditCard.setMonth(creditCard.getMonth().strip());
    creditCard.setYear(creditCard.getYear().strip());
    CreditCard creditCardSaved = creditCardRepository.save(creditCard);
    creditCardRepository.flush();
    return creditCardSaved;
  }

  /**
   * Is valid credit card number.
   *
   * @param creditCardNumber the credit card number
   * @return the boolean
   */
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


  public boolean isDueDateAfterMonthStart(CreditCard creditCard) {
    LocalDate dueDate =
        LocalDate.of(
            Integer.parseInt(creditCard.getYear()), Integer.parseInt(creditCard.getMonth()), 1);
      return dueDate.isAfter(LocalDate.now().withDayOfMonth(1));
  }
}
