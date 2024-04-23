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
        TermsConditionsFactory tcFactory = new TermsConditionsFactory();
        TermsConditions termsConditions = tcFactory.build(termsConditionsData);

        return new Voucher(code, discount, termsConditions);
    }

    public Voucher edit(Map<String, Object> request, Voucher voucher) {
        Object discountValue = request.get("discount");
        double discount;
        if (discountValue == null) {
            discount = voucher.getDiscount();
        } else {
            discount = (double)discountValue;
        }

        Map<String, Object> termsConditionsData = (Map<String, Object>)request.get("termsConditions");
        TermsConditionsFactory tcFactory = new TermsConditionsFactory();
        TermsConditions termsConditions = tcFactory.edit(termsConditionsData, voucher.getTermsConditions());

        return new Voucher(voucher.getCode(), discount, termsConditions);
    }
}
