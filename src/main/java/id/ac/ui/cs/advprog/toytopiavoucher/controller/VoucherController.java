package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.VoucherDTO;
import id.ac.ui.cs.advprog.toytopiavoucher.factory.VoucherFactory;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.service.UserService;
import id.ac.ui.cs.advprog.toytopiavoucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UserService userService;

    @Autowired
    public VoucherController(VoucherService voucherService, UserService userService) {
        this.voucherService = voucherService;
        this.userService = userService;
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
    public CompletableFuture<ResponseEntity<Voucher>> create(@RequestHeader("Authorization") String token, @RequestBody VoucherDTO voucherDTO) {
        CompletableFuture<Boolean> isAdmin = userService.isAdmin(token);
        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);
        return isAdmin.handle((result, ex) -> {
            if (ex != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!result) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Voucher created = voucherService.create(voucher);
            return ResponseEntity.ok(created);
        });
    }

    @PutMapping("/edit")
    public CompletableFuture<ResponseEntity<Voucher>> edit(@RequestHeader("Authorization") String token, @RequestBody VoucherDTO voucherDTO) {
        if (voucherDTO.getCode() == null
                || voucherDTO.getDiscount() == null
                || voucherDTO.getCreationDate() == null
                || voucherDTO.getPaymentMethod() == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }

        CompletableFuture<Boolean> isAdmin = userService.isAdmin(token);
        CompletableFuture<Boolean> found = voucherService.findByCode(voucherDTO.getCode()).thenApply(Objects::nonNull);

        VoucherFactory factory = new VoucherFactory();
        Voucher voucher = factory.create(voucherDTO);

        return isAdmin.handle((result, ex) -> {
            if (ex != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!result) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (found.join()) {
                Voucher created = voucherService.create(voucher);
                return ResponseEntity.ok(created);
            } else {
                return ResponseEntity.badRequest().build();
            }
        });
    }

    @DeleteMapping("/delete/{code}")
    public CompletableFuture<ResponseEntity<Voucher>> delete(@RequestHeader("Authorization") String token, @PathVariable(name = "code") UUID code) {
        CompletableFuture<Boolean> isAdmin = userService.isAdmin(token);
        return isAdmin.handle((result, ex) -> {
            if (ex != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!result) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Voucher deleted = voucherService.deleteByCode(code);
            if (deleted != null) {
                return ResponseEntity.ok(deleted);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @DeleteMapping("/deleteAll")
    public CompletableFuture<ResponseEntity<List<Voucher>>> deleteAll(@RequestHeader("Authorization") String token) {
        CompletableFuture<Boolean> isAdmin = userService.isAdmin(token);
        return isAdmin.handle((result, ex) -> {
            if (ex != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!result) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            List<Voucher> deleted = voucherService.findAll().join();
            voucherService.deleteAll();
            return ResponseEntity.ok(deleted);
        });
    }
}
