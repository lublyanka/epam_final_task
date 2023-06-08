package com.example.cards.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.repositories.CreditCardRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CreditCardServiceTest {
   private final CreditCardRepository creditCardRepository = mock(CreditCardRepository.class);
  private final CreditCardService creditCardService = new CreditCardService(creditCardRepository);

  private static Collection<Object[]> provideCardNumbers() {
    return Arrays.asList(
        new Object[][] {
          {"4539 1488 0343 6467", true},
          {"4539 1488 0343 a467", false},
          {"4539 1488 0343", false},
          {"4539 1488 0343 6466", false},
          {"6011111111111117", true},
          {"601111111111", false},
          {"  6011-1111-1111-1117  ", true},
          {"1", false},
          {"ABCD EFGH IJKL MNOP", false},
          {"4929 7158 6068 0103277", false},
          {"4000 0012 3456 2345 678", true}
        });
  }

  @ParameterizedTest
  @MethodSource("provideCardNumbers")
  public void testIsValidCreditCardNumber(String creditCardNumber, boolean expected) {
    assertEquals("", expected, creditCardService.isValidCreditCardNumber(creditCardNumber));
  }

  @Test
  public void testGetCreditCardReturnsEmptyOptionalWhenCreditCardNotFound() {
    String cardNumber = "1234567890123456";
    Account account = new Account();
    account.setId(UUID.randomUUID());

    when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.empty());

    Optional<CreditCard> result = creditCardService.getCreditCard(cardNumber, account);

    assertTrue("Result should be empty", result.isEmpty());
  }

  @Test
  public void testGetCreditCardReturnsEmptyOptionalWhenAccountIdsDoNotMatch() {

    String cardNumber = "1234567890123456";
    Account account = new Account();
    account.setId(UUID.randomUUID());

    CreditCard creditCard = new CreditCard();
    Account anotherAccount = new Account();
    anotherAccount.setId(UUID.randomUUID());
    creditCard.setAccount(anotherAccount);

    when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.of(creditCard));
    Optional<CreditCard> result = creditCardService.getCreditCard(cardNumber, account);

    assertTrue("Result should be empty", result.isEmpty());
  }

  @Test
  public void testGetCreditCardReturnsCreditCardWithMatchingAccountId() {

    String cardNumber = "1234567890123456";
    Account account = new Account();
    account.setId(UUID.randomUUID());

    CreditCard creditCard = new CreditCard();
    creditCard.setAccount(account);

    when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.of(creditCard));

    Optional<CreditCard> result = creditCardService.getCreditCard(cardNumber, account);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(creditCard, result.get());
    Assertions.assertEquals(account.getId(), result.get().getAccountId());
  }

  @Test
    public void testGetCreditCards() {
        // Prepare test data
        Account account = new Account();
        account.setId(UUID.randomUUID());

        // Create a list of credit cards
        List<CreditCard> creditCards = new ArrayList<>();
        CreditCard card1 = new CreditCard();
        card1.setCardNumber("1234567890123456");
        creditCards.add(card1);
        CreditCard card2 = new CreditCard();
        card2.setCardNumber("9876543210987654");
        creditCards.add(card2);

        // Set up the mock behavior of creditCardRepository.findAllByAccount
        when(creditCardRepository.findAllByAccount(account)).thenReturn(creditCards);

        // Call the getCreditCards method
        List<CreditCard> retrievedCards = creditCardService.getCreditCards(account);

        assertEquals("The same size as the list of credit cards",creditCards.size(), retrievedCards.size());
        assertEquals("First card number","3456", retrievedCards.get(0).getCardNumber());
        assertEquals("Second card number","7654", retrievedCards.get(1).getCardNumber());
        assertEquals("First card ID",account.getId(), retrievedCards.get(0).getAccountId());
        assertEquals("Second card ID",account.getId(), retrievedCards.get(1).getAccountId());
    }

    @Test
    public void testSaveCreditCard() {
        Account account = new Account();
        CreditCard creditCard = new CreditCard();
        creditCard.setCardHolder("John Doe");
        creditCard.setCardType("VISA");
        creditCard.setCardTitle("My Credit Card");
        creditCard.setValidTill(Timestamp.valueOf(LocalDate.now().plusYears(3L).withDayOfMonth(1).atStartOfDay()));

        when(creditCardRepository.save(creditCard)).thenReturn(creditCard);

        CreditCardService creditCardService = new CreditCardService(creditCardRepository);

        CreditCard savedCard = creditCardService.saveCreditCard(creditCard, account);

        Assertions.assertEquals(creditCard, savedCard);
        Assertions.assertEquals(account, savedCard.getAccount());
        Mockito.verify(creditCardRepository).flush();
    }


    @Test
    public void testIsDueDateBeforeNextMonth() {
        CreditCard creditCard = new CreditCard();
        creditCard.setValidTill(Timestamp.valueOf(LocalDate.now().plusMonths(1L).withDayOfMonth(1).atStartOfDay()));

        boolean result = creditCardService.isDueDateAfterMonthStart(creditCard);

        Assertions.assertTrue(result);
        creditCard.setValidTill(Timestamp.valueOf(LocalDate.now().minusDays(1L).withDayOfMonth(1).atStartOfDay()));
        result = creditCardService.isDueDateAfterMonthStart(creditCard);
        Assertions.assertFalse(result);
    }
}