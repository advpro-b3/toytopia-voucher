package id.ac.ui.cs.advprog.toytopiavoucher.repository;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VoucherRepositoryTest {
    private VoucherRepository voucherRepository;
    private List<Voucher> vouchers;

    @BeforeEach
    void setUp() {
        voucherRepository = new VoucherRepository();

        vouchers.add(new Voucher("c0de-1", 0.25,
                new TermsConditions(50.0, PaymentMethod.CREDIT_CARD)));

        vouchers.add(new Voucher("c0de-2", 0.50,
                new TermsConditions(20.0, PaymentMethod.BANK_TRANSFER)));

        vouchers.add(new Voucher("c0de-3", 0.40, new TermsConditions()));

        vouchers.add(new Voucher("c0de-3", 0.50,
                new TermsConditions(100.0, PaymentMethod.ANY)));
    }

    @Test
    void testCreateVoucher() {
        Voucher voucher = vouchers.getFirst();
        Voucher ret = voucherRepository.create(voucher);

        Voucher result = voucherRepository.findByCode(voucher.getCode());
        assertNotNull(ret);
        assertEquals(voucher.getCode(), result.getCode());
        assertEquals(voucher.getDiscount(), result.getDiscount());
        assertEquals(voucher.getTermsConditions(), result.getTermsConditions());
        assertTrue(voucherRepository.count() == 1);
    }

    @Test
    void testEditVoucher() {
        Voucher oldVoucher = vouchers.get(2);
        Voucher retCreate = voucherRepository.create(oldVoucher);
        Voucher newVoucher = vouchers.get(3);
        Voucher retEdit = voucherRepository.edit(newVoucher);

        Voucher result = voucherRepository.findByCode(newVoucher.getCode());
        assertNotNull(retEdit);
        assertEquals(newVoucher.getCode(), result.getCode());
        assertEquals(newVoucher.getDiscount(), result.getDiscount());
        assertEquals(newVoucher.getTermsConditions(), result.getTermsConditions());
        assertTrue(voucherRepository.count() == 1);
    }

    @Test
    void testEditVoucherNotFound() {
        Voucher oldVoucher = vouchers.get(2);
        Voucher retCreate = voucherRepository.create(oldVoucher);
        Voucher newVoucher = vouchers.get(1);
        Voucher retEdit = voucherRepository.edit(newVoucher);

        Voucher result = voucherRepository.findByCode(newVoucher.getCode());
        assertNull(retEdit);
        assertNull(result);
        assertTrue(voucherRepository.count() == 1);
    }

    @Test
    void testDeleteVoucher() {
        Voucher voucher = vouchers.getFirst();
        Voucher retCreate = voucherRepository.create(voucher);
        Voucher retDel = voucherRepository.delete(voucher);

        assertNotNull(retDel);
        assertTrue(voucherRepository.count() == 0);
    }

    @Test
    void testDeleteVoucherNotFound() {
        Voucher voucher1 = vouchers.getFirst();
        Voucher voucher2 = vouchers.get(1);
        Voucher retCreate = voucherRepository.create(voucher1);
        Voucher retDel = voucherRepository.delete(voucher2);

        assertNull(retDel);
        assertTrue(voucherRepository.count() == 1);
    }

    @Test
    void testFindByCode() {
        Voucher voucher = vouchers.getFirst();
        Voucher retCreate = voucherRepository.create(voucher);
        Voucher result = voucherRepository.findByCode(voucher.getCode());

        assertNotNull(result);
        assertEquals(voucher.getCode(), result.getCode());
        assertEquals(voucher.getDiscount(), result.getDiscount());
        assertEquals(voucher.getTermsConditions(), result.getTermsConditions());
    }

    @Test
    void testFindByCodeNotFound() {
        Voucher voucher1 = vouchers.getFirst();
        Voucher voucher2 = vouchers.get(1);
        Voucher retCreate = voucherRepository.create(voucher1);
        Voucher result = voucherRepository.findByCode(voucher2.getCode());

        assertNull(result);
    }

    @Test
    void testFindAll() {
        for (Voucher voucher : vouchers) {
            voucherRepository.create(voucher);
        }

        List<Voucher> allVouchers = voucherRepository.findAll();
        assertEquals(vouchers, allVouchers);
    }
}
