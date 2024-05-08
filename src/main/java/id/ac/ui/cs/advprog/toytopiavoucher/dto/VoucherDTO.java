package id.ac.ui.cs.advprog.toytopiavoucher.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class VoucherDTO {
    private String code;
    private Double discount;
    private Double maxDiscount;
    private Double minPurchase;
    private String paymentMethod;
    private LocalDate creationDate;
    private LocalDate expiryDate;

    @Override
    public String toString() {
        return "VoucherDTO{" +
                "code='" + code + '\'' +
                ", discount=" + discount +
                ", maxDiscount=" + maxDiscount +
                ", minPurchase=" + minPurchase +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", creationDate=" + creationDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
