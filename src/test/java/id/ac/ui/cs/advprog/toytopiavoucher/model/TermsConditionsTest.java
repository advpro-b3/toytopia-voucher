package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TermsConditionsTest {
    @Test
    void testCreateValidTC() {
        double minPrice = 100_000.0;
        PaymentMethod method = PaymentMethod.CREDIT_CARD;
        TermsConditions tc = new TermsConditions(minPrice, method);
        assertEquals(minPrice, tc.getMinimumPurchase());
        assertEquals(method, tc.getPaymentMethod());
    }
}
