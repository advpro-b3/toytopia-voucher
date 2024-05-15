package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.VoucherBuilder;
import id.ac.ui.cs.advprog.toytopiavoucher.dto.VoucherDTO;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.util.UUID;

public class VoucherFactory {
    public Voucher create(VoucherDTO voucherDTO) {
        VoucherBuilder voucherBuilder = new VoucherBuilder();
        voucherBuilder.setCode(voucherDTO.getCode())
                .setDiscount(voucherDTO.getDiscount())
                .setMaxDiscount(voucherDTO.getMaxDiscount())
                .setMinPurchase(voucherDTO.getMinPurchase())
                .setExpiryDate(voucherDTO.getExpiryDate())
                .setPaymentMethod(voucherDTO.getPaymentMethod());
        return voucherBuilder.build();
    }
}
