package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.VoucherBuilder;
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
import java.util.Optional;

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

        VoucherBuilder builder = new VoucherBuilder();

        builder.setDiscount(0.25)
                .setMinPurchase(50.0)
                .setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());
        vouchers.add(builder.build());

        builder.setDiscount(0.50)
                .setMinPurchase(20.0)
                .setPaymentMethod(PaymentMethod.BANK_TRANSFER.toString());
        vouchers.add(builder.build());

        builder.setDiscount(0.40);
        vouchers.add(builder.build());

        builder.setDiscount(0.50)
                .setMinPurchase(100.0)
                .setPaymentMethod(PaymentMethod.ANY.toString());
        vouchers.add(builder.build());
    }

    @Test
    void testCreateVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).save(voucher);

        Voucher created = voucherService.create(voucher);
        verify(voucherRepository, times(1)).save(voucher);
        assertEquals(voucher.getCode(), created.getCode());
    }

    @Test
    void testCreateExistingVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(true).when(voucherRepository).existsById(voucher.getCode());

        Voucher created = voucherService.create(voucher);
        verify(voucherRepository, times(0)).save(any(Voucher.class));
        assertNull(created);
    }

    @Test
    void testUpdateVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(voucher).when(voucherRepository).save(voucher);

        Voucher edited = voucherService.edit(voucher);
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testUpdateNonExistentVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(null).when(voucherRepository).save(voucher);

        Voucher edited = voucherService.edit(voucher);
        verify(voucherRepository, times(1)).save(any(Voucher.class));
        assertNull(edited);
    }

    @Test
    void testDeleteVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.of(voucher)).when(voucherRepository).findById(voucher.getCode());

        Voucher deleted = voucherService.delete(voucher);
        verify(voucherRepository, times(1)).delete(any(Voucher.class));
        assertEquals(voucher.getDiscount(), deleted.getDiscount());
    }

    @Test
    void testDeleteNonExistentVoucher() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.empty()).when(voucherRepository).findById(voucher.getCode());

        Voucher deleted = voucherService.delete(voucher);
        verify(voucherRepository, times(0)).delete(any(Voucher.class));
        assertNull(deleted);
    }

    @Test
    void testFindByCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.of(voucher)).when(voucherRepository).findById(voucher.getCode());

        Voucher found = voucherService.findByCode(voucher.getCode());
        assertEquals(voucher.getCode(), found.getCode());
    }

    @Test
    void testNonExistentFindByCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.empty()).when(voucherRepository).findById(voucher.getCode());

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