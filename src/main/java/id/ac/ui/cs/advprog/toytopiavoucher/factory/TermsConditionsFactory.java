package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.TermsConditionsBuilder;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;

import java.util.Map;
import java.util.Objects;

public class TermsConditionsFactory {
    public TermsConditions build(Map<String, Object> data) {
        TermsConditionsBuilder tcBuilder = new TermsConditionsBuilder();
        setFields(tcBuilder, data);
        return tcBuilder.build();
    }

    public TermsConditions edit(Map<String, Object> data, TermsConditions tc) {
       TermsConditionsBuilder tcBuilder = new TermsConditionsBuilder(tc);
       setFields(tcBuilder, data);
       return tcBuilder.build();
    }

    private void setFields(TermsConditionsBuilder tcBuilder, Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (key.equals("minimumPurchase")) {
                double minimumPurchase = (double)entry.getValue();
                tcBuilder.setMinimumPurchase(minimumPurchase);
            } else if (key.equals("paymentMethod")) {
                PaymentMethod method = PaymentMethod.fromString((String) entry.getValue());
                tcBuilder.setPaymentMethod(method);
            }
        }
    }
}
