package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.VoucherDTO;
import id.ac.ui.cs.advprog.toytopiavoucher.factory.VoucherFactory;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    private VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Voucher>>> getAll() throws Exception {
        return voucherService.findAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{code}")
    public CompletableFuture<ResponseEntity<Voucher>> getByCode(@PathVariable(name = "code") UUID code) {
        return voucherService.findByCode(code).thenApply((result) -> {
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @PostMapping("/create")
    public ResponseEntity<Voucher> create(@RequestBody VoucherDTO voucherDTO) {
        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);
        Voucher created = voucherService.create(voucher);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/edit")
    public ResponseEntity<Voucher> edit(@RequestBody VoucherDTO voucherDTO) {
        if (voucherDTO.getCode() == null
                || voucherDTO.getDiscount() == null
                || voucherDTO.getCreationDate() == null
                || voucherDTO.getPaymentMethod() == null) {
            return ResponseEntity.badRequest().build();
        }

        CompletableFuture<Boolean> found = voucherService.findByCode(voucherDTO.getCode()).thenApply(Objects::nonNull);
        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);

        if (found.join() != null) {
            Voucher created = voucherService.create(voucher);
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<Voucher> delete(@PathVariable(name = "code") UUID code) {
        Voucher deleted = voucherService.deleteByCode(code);
        if (deleted != null) {
            return ResponseEntity.ok(deleted);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteAll")
    public CompletableFuture<ResponseEntity<List<Voucher>>> deleteAll() {
        return voucherService.findAll().thenApply((result) -> {
            voucherService.deleteAll();
            return ResponseEntity.ok(result);
        });
    }
}
