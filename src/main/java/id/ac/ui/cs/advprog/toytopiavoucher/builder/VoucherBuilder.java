package id.ac.ui.cs.advprog.toytopiavoucher.builder;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class VoucherBuilder {
    private Voucher voucher;

    public VoucherBuilder() {
        this.reset();
    }

    private void reset() {
        this.voucher = new Voucher();
    }

    public VoucherBuilder setCode(UUID code) {
        this.voucher.setCode(code);
        return this;
    }

    public VoucherBuilder setDiscount(Double discount) {
        this.voucher.setDiscount(discount);
        return this;
    }

    public VoucherBuilder setMaxDiscount(Double maxDiscount) {
        this.voucher.setMaxDiscount(maxDiscount);
        return this;
    }

    public VoucherBuilder setMinPurchase(Double minPurchase) {
        this.voucher.setMinPurchase(minPurchase);
        return this;
    }

    public VoucherBuilder setPaymentMethod(String paymentMethod) {
        if (!PaymentMethod.contains(paymentMethod)) {
            throw new IllegalArgumentException();
        }
        this.voucher.setPaymentMethod(PaymentMethod.fromString(paymentMethod));
        return this;
    }

    public VoucherBuilder setCreationDate(LocalDate creationDate) {
        this.voucher.setCreationDate(creationDate);
        return this;
    }

    public VoucherBuilder setExpiryDate(LocalDate expiryDate) {
        this.voucher.setExpiryDate(expiryDate);
        return this;
    }

    public Voucher build() {
        if (this.voucher.getDiscount() == null) {
            throw new IllegalStateException();
        }
        Voucher res = this.voucher;
        this.reset();
        return res;
    }
}
