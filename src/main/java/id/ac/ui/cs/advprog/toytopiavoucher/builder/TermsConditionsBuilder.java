package id.ac.ui.cs.advprog.toytopiavoucher.builder;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;

public class TermsConditionsBuilder {
    private TermsConditions termsConditions;

    public TermsConditionsBuilder() {
        this.reset();
    }

    public void reset() {
        this.termsConditions = new TermsConditions();
    }

    public void setMinimumPurchase(double amount) {
        termsConditions.setMinimumPurchase(amount);
    }

    public void setPaymentMethod(PaymentMethod method) {
        termsConditions.setPaymentMethod(method);
    }

    public TermsConditions build() {
        TermsConditions result = this.termsConditions;
        this.reset();
        return result;
    }
}
