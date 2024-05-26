package id.ac.ui.cs.advprog.toytopiavoucher.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class VoucherDTO {
    private UUID code;
    private Double discount;
    private Double maxDiscount;
    private Double minPurchase;
    private String paymentMethod;
    private LocalDate creationDate;
    private LocalDate expiryDate;
}
