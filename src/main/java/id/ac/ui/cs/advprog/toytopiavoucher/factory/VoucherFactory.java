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

        Map<String, String> termsConditionsData = (Map<String, String>)request.get("termsConditions");
        TermsConditionsFactory tcFactory = new TermsConditionsFactory();
        TermsConditions termsConditions = tcFactory.build(termsConditionsData);

        return new Voucher(code, discount, termsConditions);
    }
}
