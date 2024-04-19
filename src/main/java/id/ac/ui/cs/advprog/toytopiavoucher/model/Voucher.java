package id.ac.ui.cs.advprog.toytopiavoucher.model;

import lombok.Getter;

@Getter
public class Voucher {
    private String code;
    private double discount;
    TermsConditions termsConditions;

    public Voucher(String code, double discount, TermsConditions termsConditions) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (discount <= 0 || discount > 1) {
            throw new IllegalArgumentException();
        }
        this.code = code;
        this.discount = discount;
        this.termsConditions = termsConditions;
    }

    public Voucher(String code, double discount) {
        this(code, discount, new TermsConditions());
    }
}
