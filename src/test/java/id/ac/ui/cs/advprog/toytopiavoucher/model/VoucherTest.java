package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import jakarta.persistence.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class VoucherTest {
    private UUID code;
    private double discount;
    private Double maxDiscount;
    private Double minPurchase;
    private PaymentMethod paymentMethod;
    private LocalDate creationDate;
    private LocalDate expiryDate;

    @BeforeEach
    void setUp() {
        code = UUID.randomUUID();
        discount = 0.5;
        maxDiscount = 50_000.0;
        minPurchase = 100_000.0;
        paymentMethod = PaymentMethod.CREDIT_CARD;
        creationDate = LocalDate.now();
        expiryDate = creationDate.plusMonths(1);
    }

    @Test
    void testCreateEmptyVoucher() {
        Voucher v = new Voucher();
        assertNull(v.getCode());
        assertNotNull(v.getCreationDate());
        assertNotNull(v.getPaymentMethod());
    }

    @Test
    void testCreateValidVoucher() {
        Voucher v = new Voucher(discount, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate);
        assertNull(v.getCode());
        assertEquals(discount, v.getDiscount());
        assertEquals(maxDiscount, v.getMaxDiscount());
        assertEquals(minPurchase, v.getMinPurchase());
        assertEquals(paymentMethod, v.getPaymentMethod());
        assertEquals(creationDate, v.getCreationDate());
        assertEquals(expiryDate, v.getExpiryDate());
    }

    @Test
    void testCreateInvalidDiscount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(0.0, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(1.1, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(-1.0, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate));
    }

    @Test
    void testInvalidMaxDiscount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(discount, -1.0, minPurchase, paymentMethod, creationDate, expiryDate));
    }

    @Test
    void testInvalidMinPurchase() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(discount, maxDiscount, -1.0, paymentMethod, creationDate, expiryDate));
    }

    @Test
    void testInvalidExpiryDate() {
        expiryDate = creationDate.minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(discount, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate));
    }

    @Test
    void testNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(null, maxDiscount, minPurchase, paymentMethod, creationDate, expiryDate));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(discount, maxDiscount, minPurchase, null, creationDate, expiryDate));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(discount, maxDiscount, minPurchase, paymentMethod, null, expiryDate));
    }
}
