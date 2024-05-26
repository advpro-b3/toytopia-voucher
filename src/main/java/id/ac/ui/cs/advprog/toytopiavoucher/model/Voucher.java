package id.ac.ui.cs.advprog.toytopiavoucher.model;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "VOUCHER")
@Table(name = "VOUCHER")
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
    @Column(columnDefinition = "smallint check (payment_method between 0 and 3) default 0")
    private PaymentMethod paymentMethod;
    @Column(columnDefinition = "date default current_date")
    private LocalDate creationDate;
    @Column
    private LocalDate expiryDate;

    public Voucher() {
        this.creationDate = LocalDate.now();
        this.paymentMethod = PaymentMethod.ANY;
    }

    public Voucher(Double discount, Double maxDiscount, Double minPurchase, PaymentMethod paymentMethod, LocalDate creationDate, LocalDate expiryDate) {
        if (discount == null || paymentMethod == null || creationDate == null) {
            throw new IllegalArgumentException();
        }
        setDiscount(discount);
        setMaxDiscount(maxDiscount);
        setMinPurchase(minPurchase);
        this.paymentMethod = paymentMethod;
        this.creationDate = creationDate;
        setExpiryDate(expiryDate);
    }

    public void setDiscount(Double discount) {
        if (discount == null || discount <= 0.0 || discount > 1.0) {
            throw new IllegalArgumentException();
        }
        this.discount = discount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        if (maxDiscount != null && maxDiscount < 0.0) {
            throw new IllegalArgumentException();
        }
        this.maxDiscount = maxDiscount;
    }

    public void setMinPurchase(Double minPurchase) {
        if (minPurchase != null && minPurchase < 0.0) {
            throw new IllegalArgumentException();
        }
        this.minPurchase = minPurchase;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && (expiryDate.isBefore(creationDate))) {
            throw new IllegalArgumentException();
        }
        this.expiryDate = expiryDate;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = Objects.requireNonNullElse(paymentMethod, PaymentMethod.ANY);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Voucher v)) {
            return false;
        } else {
            return this.code.equals(v.code);
        }
    }
}
