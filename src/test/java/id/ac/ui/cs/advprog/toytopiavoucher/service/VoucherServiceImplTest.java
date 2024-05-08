package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceImplTest {
    @InjectMocks
    private VoucherServiceImpl voucherService;
    @Mock
    private VoucherRepository voucherRepository;
    private List<Voucher> vouchers;

    @BeforeEach
    void setUp() {
        vouchers = new ArrayList<>();

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
        doReturn(voucher).when(voucherRepository).create(voucher);

        Voucher created = voucherService.create(voucher);
        verify(voucherRepository, times(1)).create(voucher);
        assertEquals(voucher.getCode(), created.getCode());
        assertEquals(voucher.getDiscount(), created.getDiscount());
        assertEquals(voucher.getTermsConditions(), created.getTermsConditions());
    }

    @Test
    void testCreateExistingVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).findByCode(voucher.getCode());

        Voucher created = voucherService.create(voucher);
        verify(voucherRepository, times(0)).create(any(Voucher.class));
        verify(voucherRepository, times(1)).findByCode(voucher.getCode());
        assertNull(created);
    }

    @Test
    void testUpdateVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).edit(voucher);

        Voucher edited = voucherService.edit(voucher);
        verify(voucherRepository, times(1)).edit(any(Voucher.class));
    }

    @Test
    void testUpdateNonExistentVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(null).when(voucherRepository).edit(voucher);

        Voucher edited = voucherService.edit(voucher);
        verify(voucherRepository, times(1)).edit(any(Voucher.class));
        assertNull(edited);
    }

    @Test
    void testDeleteVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).delete(voucher);

        Voucher deleted = voucherService.delete(voucher);
        verify(voucherRepository, times(1)).delete(any(Voucher.class));
    }

    @Test
    void testDeleteNonExistentVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(null).when(voucherRepository).delete(voucher);

        Voucher deleted = voucherService.delete(voucher);
        verify(voucherRepository, times(1)).delete(any(Voucher.class));
        assertNull(deleted);
    }

    @Test
    void testFindByCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).findByCode(voucher.getCode());

        Voucher found = voucherService.findByCode(voucher.getCode());
        assertEquals(voucher.getCode(), found.getCode());
    }

    @Test
    void testNonExistentFindByCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(null).when(voucherRepository).findByCode(voucher.getCode());

        Voucher found = voucherService.findByCode(voucher.getCode());
        assertNull(found);
    }

    @Test
    void testFindAll() {
        doReturn(vouchers).when(voucherRepository).findAll();
        List<Voucher> all = voucherService.findAll();
        assertEquals(vouchers, all);
    }
}