package com.example.cards.requests;

import com.example.cards.enums.PaymentStatus;
import com.example.cards.repositories.dict.CurrencyRepository;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentRequest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Getter
    @Setter
    private UUID accountId;
    @Getter
    @Setter
    private PaymentStatus status;
    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String currencyCode;
    @Getter
    @Setter
    private Long userId;

    public boolean isValid() {
        return this.accountId != null &&
                this.amount != null &&
                this.amount.compareTo(BigDecimal.ZERO) > 0 &&
                currencyRepository.existsById(this.getCurrencyCode());
    }
}
