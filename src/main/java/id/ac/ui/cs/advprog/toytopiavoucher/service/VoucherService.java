package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.util.List;

public interface VoucherService {
    Voucher create(Voucher voucher);
    Voucher edit(Voucher voucher);
    Voucher delete(Voucher voucher);
    Voucher findByCode(String code);
    List<Voucher> findAll();
}
