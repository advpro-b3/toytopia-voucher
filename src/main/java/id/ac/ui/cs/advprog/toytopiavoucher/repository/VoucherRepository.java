package id.ac.ui.cs.advprog.toytopiavoucher.repository;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherRepository {
    private List<Voucher> voucherData;

    public VoucherRepository() {
        voucherData = new ArrayList<>();
    }

    public Voucher create(Voucher voucher) {
        voucherData.add(voucher);
        return voucher;
    }

    public Voucher edit(Voucher voucher) {
        Voucher found = findByCode(voucher.getCode());
        if (found != null) {
            voucherData.remove(found);
            voucherData.add(voucher);
        }
        return found;
    }

    public Voucher delete(Voucher voucher) {
        Voucher found = findByCode(voucher.getCode());
        if (found != null) {
            voucherData.remove(found);
        }
        return found;
    }

    public Voucher findByCode(String code) {
        for (Voucher voucher : voucherData) {
            if (voucher.getCode().equals(code)) {
                return voucher;
            }
        }
        return null;
    }

    public List<Voucher> findAll() {
        return new ArrayList<>(voucherData);
    }

    public int count() {
        return voucherData.size();
    }
}
