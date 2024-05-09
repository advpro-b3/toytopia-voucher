package id.ac.ui.cs.advprog.toytopiavoucher.builder;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.dnd.InvalidDnDOperationException;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class VoucherBuilderTest {
    private VoucherBuilder builder;
    private UUID code;
    private double discount;
    private Double maxDiscount;
    private Double minPurchase;
    private String paymentMethod;
    private LocalDate creationDate;
    private LocalDate expiryDate;

    @BeforeEach
    void setUp() {
        builder = new VoucherBuilder();
        code = UUID.randomUUID();
        discount = 0.5;
        maxDiscount = 50_000.0;
        minPurchase = 100_000.0;
        paymentMethod = PaymentMethod.CREDIT_CARD.toString();
        creationDate = LocalDate.now();
        expiryDate = creationDate.plusMonths(1);

        builder.setDiscount(discount);
    }

    @Test
    void testCode() {
        builder.setCode(code);
        Voucher v = builder.build();
        assertEquals(code, v.getCode());
    }

    @Test
    void testNoDiscount() {
        builder = new VoucherBuilder();
        assertThrows(IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void testDiscount() {
        builder.setDiscount(discount);
        Voucher v = builder.build();
        assertEquals(discount, v.getDiscount());
    }

    @Test
    void testInvalidDiscount() {
        assertThrows(IllegalArgumentException.class,
                () -> builder.setDiscount(1.1));
        assertThrows(IllegalArgumentException.class,
                () -> builder.setDiscount(-1.0));
    }

    @Test
    void testNoMaxDiscount() {
        Voucher v = builder.build();
        assertNull(v.getMaxDiscount());
    }

    @Test
    void testMaxDiscount() {
        builder.setMaxDiscount(maxDiscount);
        Voucher v = builder.build();
        assertEquals(maxDiscount, v.getMaxDiscount());
    }

    @Test
    void testInvalidMaxDiscount() {
        assertThrows(IllegalArgumentException.class,
                () ->builder.setMaxDiscount(-1.0));
    }

    @Test
    void testNoMinPurchase() {
        Voucher v = builder.build();
        assertNull(v.getMinPurchase());
    }

    @Test
    void testMinPurchase() {
        builder.setMinPurchase(minPurchase);
        Voucher v = builder.build();
        assertEquals(minPurchase, v.getMinPurchase());
    }

    @Test
    void testInvalidMinPurchase() {
        assertThrows(IllegalArgumentException.class,
                () -> builder.setMinPurchase(-1.0));
    }

    @Test
    void testNoPaymentMethod() {
        Voucher v = builder.build();
        assertEquals(PaymentMethod.ANY, v.getPaymentMethod());
    }

    @Test
    void testPaymentMethod() {
        builder.setPaymentMethod(paymentMethod);
        Voucher v = builder.build();
        assertEquals(PaymentMethod.fromString(paymentMethod), v.getPaymentMethod());
    }

    @Test
    void testInvalidPaymentMethod() {
        assertThrows(IllegalArgumentException.class,
                () -> builder.setPaymentMethod("SKIBIDI_TOILET_RIZZ"));
    }

    @Test
    void testNoCreationDate() {
        Voucher v = builder.build();
        assertNotNull(v.getCreationDate());
    }

    @Test
    void testCreationDate() {
        builder.setCreationDate(creationDate);
        Voucher v = builder.build();
        assertEquals(creationDate, v.getCreationDate());
    }

    @Test
    void testNoExpiryDate() {
        Voucher v = builder.build();
        assertNull(v.getExpiryDate());
    }

    @Test
    void testExpiryDate() {
        builder.setExpiryDate(expiryDate);
        Voucher v = builder.build();
        assertEquals(expiryDate, v.getExpiryDate());
    }

    @Test
    void testInvalidExpiryDate() {
        expiryDate = creationDate.minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> builder.setExpiryDate(expiryDate));
    }
}
