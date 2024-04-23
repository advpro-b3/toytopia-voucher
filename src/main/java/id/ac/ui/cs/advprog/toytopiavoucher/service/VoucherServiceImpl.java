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
        if (voucherRepository.findByCode(voucher.getCode()) == null) {
            return voucherRepository.create(voucher);
        }
        return null;
    }

    @Override
    public Voucher edit(Voucher voucher) {
        return voucherRepository.edit(voucher);
    }

    @Override
    public Voucher delete(Voucher voucher) {
        return voucherRepository.delete(voucher);
    }

    @Override
    public Voucher findByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }
}
