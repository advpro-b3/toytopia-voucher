package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.util.List;
import java.util.UUID;

public interface VoucherService {
    Voucher create(Voucher voucher);
    Voucher edit(Voucher voucher);
    Voucher delete(Voucher voucher);
    Voucher deleteByCode(UUID code);
    void deleteAll();
    Voucher findByCode(UUID code);
    List<Voucher> findAll();
}
