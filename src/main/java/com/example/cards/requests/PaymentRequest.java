package com.example.cards.requests;

import com.example.cards.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** The type Payment request. */
public class PaymentRequest {

  //@Autowired private CurrencyRepository currencyRepository;

  @Getter @Setter private UUID accountId;
  @Getter @Setter private PaymentStatus status;
  @Getter @Setter private BigDecimal amount;
  @Getter @Setter private String description;
  @Getter @Setter private String currency;
  @Getter @Setter private String number;
  @Getter @Setter private Long userId;

  /**
   * Is payment valid.
   *
   * @return the boolean
   */
  public boolean isValid() {
    return this.accountId != null
        && this.number != null
        && this.amount != null
        && this.amount.compareTo(BigDecimal.ZERO) > 0;
  }
}
