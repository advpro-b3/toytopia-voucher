package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.TermsConditionsBuilder;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;

import java.util.Map;

public class TermsConditionsFactory {
    public TermsConditions build(Map<String, String> data) {
        TermsConditionsBuilder tcBuilder = new TermsConditionsBuilder();
        setFields(tcBuilder, data);
        return tcBuilder.build();
    }

    public TermsConditions edit(Map<String, String> data, TermsConditions tc) {
       TermsConditionsBuilder tcBuilder = new TermsConditionsBuilder(tc);
       setFields(tcBuilder, data);
       return tcBuilder.build();
    }

    private void setFields(TermsConditionsBuilder tcBuilder, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            if (key.equals("minimumPurchase")) {
                double minimumPurchase = Double.parseDouble(entry.getValue());
                tcBuilder.setMinimumPurchase(minimumPurchase);
            } else if (key.equals("paymentMethod")) {
                PaymentMethod method = PaymentMethod.fromString((String) entry.getValue());
                tcBuilder.setPaymentMethod(method);
            }
        }
    }
}
