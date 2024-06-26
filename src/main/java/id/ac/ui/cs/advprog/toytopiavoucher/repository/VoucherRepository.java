package id.ac.ui.cs.advprog.toytopiavoucher.repository;

import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {

}
