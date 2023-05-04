package com.example.cards;

import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.example.cards.entities.Account;
import com.example.cards.entities.CreditCard;
import com.example.cards.repositories.CreditCardRepository;
import com.example.cards.repositories.dict.CurrencyRepository;
import com.example.cards.services.CreditCardService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @ActiveProfiles("test")
public class CreditCardServiceTest {
  private final CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
  private final CreditCardRepository creditCardRepository = mock(CreditCardRepository.class);
  private final CreditCardService creditCardService = new CreditCardService(creditCardRepository, currencyRepository);

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
    // Arrange
    String cardNumber = "1234567890123456";
    Account account = new Account();
    account.setId(UUID.randomUUID());

    Mockito.when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.empty());

    // Act
    Optional<CreditCard> result = creditCardService.getCreditCard(cardNumber, account);

    // Assert
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

    Mockito.when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.of(creditCard));
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

    Mockito.when(creditCardRepository.findById(cardNumber)).thenReturn(Optional.of(creditCard));

    Optional<CreditCard> result = creditCardService.getCreditCard(cardNumber, account);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(creditCard, result.get());
    Assertions.assertEquals(account.getId(), result.get().getAccountId());
  }

//  @TestConfiguration
//  class CreditCardServiceTestContextConfiguration {
//
//    @MockBean private CreditCardRepository creditCardRepository;
//
//    @MockBean private CurrencyRepository currencyRepository;
//
//    @Bean
//    public CreditCardService creditCardService() {
//      return new CreditCardService();
//    }
//  }
}
