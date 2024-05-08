package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.VoucherDTO;
import id.ac.ui.cs.advprog.toytopiavoucher.factory.VoucherFactory;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    private VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Voucher>> getAll() {
        List<Voucher> all = voucherService.findAll();
        if (all != null) {
            return ResponseEntity.ok(all);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<Voucher> getByCode(@PathVariable(name = "code") UUID code) {
        Voucher voucher = voucherService.findByCode(code);
        if (voucher != null) {
            return ResponseEntity.ok(voucher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Voucher> create(@RequestBody VoucherDTO voucherDTO) {
        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);
        Voucher created = voucherService.create(voucher);
        if (created != null) {
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Voucher> edit(@RequestBody VoucherDTO voucherDTO) {
        if (voucherDTO.getCode() == null) {
            return ResponseEntity.badRequest().build();
        }

        UUID code = UUID.fromString(voucherDTO.getCode());
        Voucher found = voucherService.findByCode(code);
        if (found == null) {
            return ResponseEntity.badRequest().build();
        }

        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);
        Voucher created = voucherService.create(voucher);
        return ResponseEntity.ok(created);
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
}
