package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import lombok.Getter;

@Getter
public class TermsConditions {
    private double minimumPurchase;
    private PaymentMethod paymentMethod;

    public TermsConditions(double minimumPurchase, PaymentMethod paymentMethod) {
        if (minimumPurchase < 0 || paymentMethod == null) {
            throw new IllegalArgumentException();
        }
        this.minimumPurchase = minimumPurchase;
        this.paymentMethod = paymentMethod;
    }
}
