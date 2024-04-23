package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    private VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher create(Voucher voucher) {
        return null;
    }

    @Override
    public Voucher edit(Voucher voucher) {
        return null;
    }

    @Override
    public Voucher delete(Voucher voucher) {
        return null;
    }

    @Override
    public Voucher findByCode(String code) {
        return null;
    }

    @Override
    public List<Voucher> findAll() {
        return null;
    }
}
