package id.ac.ui.cs.advprog.toytopiavoucher;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.repository.VoucherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class ToytopiavoucherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToytopiavoucherApplication.class, args);
    }
}
