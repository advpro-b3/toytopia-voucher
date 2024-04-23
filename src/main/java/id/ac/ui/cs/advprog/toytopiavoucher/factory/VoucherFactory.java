package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.TermsConditionsBuilder;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.util.Map;

public class VoucherFactory {
    public Voucher create(Map<String, Object> request) {
        String code = (String)request.get("code");
        double discount = (double)request.get("discount");

        Map<String, Object> termsConditionsData = (Map<String, Object>)request.get("termsConditions");
        TermsConditions termsConditions = buildTermsConditions(termsConditionsData);

        return new Voucher(code, discount, termsConditions);
    }

    private TermsConditions buildTermsConditions(Map<String, Object> data) {
        TermsConditionsBuilder tcBuilder = new TermsConditionsBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (key.equals("minimumPurchase")) {
                System.out.println(entry.getValue());
                double minimumPurchase = (double)entry.getValue();
                tcBuilder.setMinimumPurchase(minimumPurchase);
            } else if (key.equals("paymentMethod")) {
                PaymentMethod method = PaymentMethod.fromString((String)entry.getValue());
                tcBuilder.setPaymentMethod(method);
            }
        }
        return tcBuilder.build();
    }
}
