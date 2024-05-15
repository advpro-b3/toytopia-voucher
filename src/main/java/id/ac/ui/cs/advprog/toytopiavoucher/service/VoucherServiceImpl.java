package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher create(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher edit(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher delete(Voucher voucher) {
        Optional<Voucher> found = voucherRepository.findById(voucher.getCode());
        if (found.isPresent()) {
            voucherRepository.delete(voucher);
            return found.get();
        }
        return null;
    }

    @Override
    public Voucher deleteByCode(UUID code) {
        Optional<Voucher> found = voucherRepository.findById(code);
        if (found.isPresent()) {
            voucherRepository.deleteById(code);
            return found.get();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        voucherRepository.deleteAll();
    }

    @Override
    public Voucher findByCode(UUID code) {
        return voucherRepository.findById(code).orElse(null);
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }
}
