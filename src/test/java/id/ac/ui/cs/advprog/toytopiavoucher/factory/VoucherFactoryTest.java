package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.VoucherDTO;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
class VoucherFactoryTest {
    private VoucherFactory voucherFactory;
    private VoucherDTO voucherDTO;

    @BeforeEach
    void setUp() {
        voucherFactory = new VoucherFactory();
        voucherDTO = new VoucherDTO();
        voucherDTO.setCode(UUID.randomUUID());
        voucherDTO.setDiscount(0.5);
        voucherDTO.setMinPurchase(100_000.0);
        voucherDTO.setMaxDiscount(50_000.0);
        voucherDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());
        voucherDTO.setCreationDate(LocalDate.now());
        voucherDTO.setExpiryDate(LocalDate.now().plusWeeks(1));
    }

    @Test
    void testCreateFull() {
        Voucher v = voucherFactory.create(voucherDTO);
        assertEquals(voucherDTO.getCode(), v.getCode());
        assertEquals(voucherDTO.getDiscount(), v.getDiscount());
        assertEquals(voucherDTO.getMinPurchase(), v.getMinPurchase());
        assertEquals(voucherDTO.getMaxDiscount(), v.getMaxDiscount());
        assertEquals(PaymentMethod.fromString(voucherDTO.getPaymentMethod()), v.getPaymentMethod());
        assertEquals(voucherDTO.getCreationDate(), v.getCreationDate());
        assertEquals(voucherDTO.getExpiryDate(), v.getExpiryDate());
    }

    @Test
    void testCreatePartial() {
        voucherDTO = new VoucherDTO();
        voucherDTO.setDiscount(0.5);
        voucherDTO.setMinPurchase(100_000.0);
        voucherDTO.setCreationDate(LocalDate.now());
        voucherDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD.toString());

        Voucher v = voucherFactory.create(voucherDTO);

        assertNull(v.getCode());
        assertEquals(voucherDTO.getDiscount(), v.getDiscount());
        assertEquals(voucherDTO.getMinPurchase(), v.getMinPurchase());
        assertNull(v.getMaxDiscount());
        assertEquals(PaymentMethod.fromString(voucherDTO.getPaymentMethod()), v.getPaymentMethod());
        assertEquals(voucherDTO.getCreationDate(), v.getCreationDate());
        assertNull(v.getExpiryDate());
    }
}
