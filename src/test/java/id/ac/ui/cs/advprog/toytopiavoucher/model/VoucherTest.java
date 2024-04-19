package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VoucherTest {
    private String code;
    private double discount;
    private TermsConditions tc;

    @BeforeEach
    void setUp() {
        code = "SHRIGMA-MALE-123";
        discount = 0.5;
        tc = new TermsConditions(50_000.0, PaymentMethod.ANY);
    }

    @Test
    void testCreateValidVoucher() {
        Voucher v = new Voucher(code, discount, tc);
        assertEquals(code, v.getCode());
        assertEquals(discount, v.getDiscount());
        assertEquals(tc, v.getTermsConditions());
    }

    @Test
    void testCreateWithNoTc() {
        Voucher v = new Voucher(code, discount);
        TermsConditions emptyTc = new TermsConditions();
        assertEquals(emptyTc, v.getTermsConditions());
    }

    @Test
    void testCreateWithEmptyOrNullCode() {
        code = "";
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(code, discount, tc));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(null, discount, tc));
    }

    @Test
    void testCreateWithZeroOrLessDiscount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(code, 0.0, tc));
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(code, -1.0, tc));
    }

    @Test
    void testCreateWithGreaterThanOneDiscount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Voucher(code, 1.1, tc));
    }
}
