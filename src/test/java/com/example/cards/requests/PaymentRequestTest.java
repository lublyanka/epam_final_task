package com.example.cards.requests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.cards.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentRequestTest {

    PaymentRequest paymentRequest;
    @BeforeEach void createPayment(){
        paymentRequest = new PaymentRequest();
        paymentRequest.setAccountId(UUID.randomUUID());
        paymentRequest.setStatus(PaymentStatus.PREPARED);
        paymentRequest.setAmount(BigDecimal.valueOf(100.0));
        paymentRequest.setDescription("Payment description");
        paymentRequest.setCurrency("USD");
        paymentRequest.setNumber("123456");
        paymentRequest.setUserId(123L);
    }
    @Test
    public void testIsValid_ValidPaymentRequest_ReturnsTrue() {
        boolean isValid = paymentRequest.isValid();
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_MissingRequiredField_ReturnsFalse() {
        // Missing number field
        paymentRequest.setNumber(null);

        boolean isValid = paymentRequest.isValid();
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_InvalidAmount_ReturnsFalse() {
        // Amount is zero
        paymentRequest.setAmount(BigDecimal.ZERO);

        boolean isValid = paymentRequest.isValid();
        assertFalse(isValid);
    }
}