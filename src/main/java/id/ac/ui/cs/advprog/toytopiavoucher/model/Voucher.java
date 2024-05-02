package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity(name = "voucher")
@Table(name = "voucher")
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID code;
    @Column(nullable = false, columnDefinition = "float(53) CHECK(discount between 0.0 and 1.0)")
    private Double discount;
    @Column
    private Double maxDiscount;
    @Column
    private Double minPurchase;
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    @Column
    private LocalDate creationDate;
    @Column
    private LocalDate expiryDate;

    public Voucher() {
        this.paymentMethod = PaymentMethod.ANY;
        this.creationDate = LocalDate.now();
    }

    public Voucher(Double discount, Double maxDiscount, Double minPurchase, PaymentMethod paymentMethod, LocalDate creationDate, LocalDate expiryDate) {
        this.discount = discount;
        this.maxDiscount = maxDiscount;
        this.minPurchase = minPurchase;
        this.paymentMethod = paymentMethod;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
    }
}
