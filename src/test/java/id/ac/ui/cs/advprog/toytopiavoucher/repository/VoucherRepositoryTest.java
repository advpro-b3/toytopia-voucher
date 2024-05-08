package id.ac.ui.cs.advprog.toytopiavoucher.repository;

import id.ac.ui.cs.advprog.toytopiavoucher.builder.VoucherBuilder;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VoucherRepositoryTest {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private TestEntityManager entityManager;
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
    void testSaveVoucher() {
        Voucher voucher = vouchers.getFirst();
        Voucher inserted = voucherRepository.save(voucher);
        Voucher retrieved = entityManager.find(Voucher.class, inserted.getCode());

        assertNotNull(inserted);
        assertEquals(voucher.getCode(), retrieved.getCode());
        assertEquals(1, voucherRepository.count());
    }

    @Test
    void testUpdateVoucher() {
        Voucher voucher = vouchers.getFirst();
        entityManager.persist(voucher);
        voucher.setDiscount(0.9);
        Voucher updated = voucherRepository.save(voucher);

        Voucher result = entityManager.find(Voucher.class, updated.getCode());
        assertNotNull(updated);
        assertEquals(voucher.getCode(), result.getCode());
        assertEquals(voucher.getDiscount(), result.getDiscount());
        assertEquals(1, voucherRepository.count());
    }

    @Test
    void testFindByCode() {
        Voucher voucher = vouchers.getFirst();
        entityManager.persist(voucher);
        Optional<Voucher> retrieved = voucherRepository.findById(voucher.getCode());
        assertThat(retrieved).contains(voucher);
    }

    @Test
    void testDeleteVoucher() {
        Voucher voucher = vouchers.getFirst();
        entityManager.persist(voucher);
        voucherRepository.delete(voucher);
        assertThat(entityManager.find(Voucher.class, voucher.getCode())).isNull();
    }
}
