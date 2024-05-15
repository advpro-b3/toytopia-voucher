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
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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
                .setCreationDate(LocalDate.now())
                .setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());
        vouchers.add(builder.build());

        builder.setDiscount(0.50)
                .setMinPurchase(20.0)
                .setCreationDate(LocalDate.now())
                .setPaymentMethod(PaymentMethod.BANK_TRANSFER.toString());
        vouchers.add(builder.build());

        builder.setDiscount(0.40)
                .setCreationDate(LocalDate.now())
                .setPaymentMethod(PaymentMethod.ANY.toString());
        vouchers.add(builder.build());

        builder.setDiscount(0.50)
                .setMinPurchase(100.0)
                .setCreationDate(LocalDate.now())
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
        verify(voucherRepository, times(1)).delete(voucher);
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
    void testDeleteVoucherByCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.of(voucher)).when(voucherRepository).findById(voucher.getCode());

        Voucher deleted = voucherService.deleteByCode(voucher.getCode());
        verify(voucherRepository, times(1)).deleteById(voucher.getCode());
        assertEquals(voucher.getDiscount(), deleted.getDiscount());
    }

    @Test
    void testDeleteNonExistentCode() {
        Voucher voucher = vouchers.getFirst();
        doReturn(Optional.empty()).when(voucherRepository).findById(voucher.getCode());

        Voucher deleted = voucherService.deleteByCode(voucher.getCode());
        verify(voucherRepository, times(0)).deleteById(any(UUID.class));
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