package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import com.jayway.jsonpath.JsonPath;
import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.service.VoucherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(VoucherController.class)
@ActiveProfiles("test")
public class VoucherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VoucherService service;
    private Voucher voucher;
    private UUID code;
    private String json;
    private LocalDate expiryDate;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        expiryDate = LocalDate.now().plusMonths(1);
        voucher = new Voucher();
        code = UUID.randomUUID();
        voucher.setCode(code);
        voucher.setDiscount(0.3);
        voucher.setMinPurchase(150000.0);
        voucher.setExpiryDate(LocalDate.now().plusMonths(1));
        voucher.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        json = """
                {
                    "discount": 0.3,
                    "minPurchase": 150000.0,
                    "expiryDate": "%s",
                    "paymentMethod": "CREDIT_CARD"
                }""";
        json = String.format(json, expiryDate.format(formatter));
    }

    @Test
    void shouldReturnOneVoucher() throws Exception {
        when(service.findAll()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(voucher)));

        MvcResult mvcResult = this.mockMvc.perform(get("/voucher/all"))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value(code.toString()));
    }

    @Test
    void shouldReturnSelectedVoucher() throws Exception {
        when(service.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));

        MvcResult mvcResult = this.mockMvc.perform(get(String.format("/voucher/%s", code)))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnNotFoundIfVoucherNotExist() throws Exception {
        when(service.findByCode(any(UUID.class))).thenReturn(CompletableFuture.completedFuture(null));

        MvcResult mvcResult = this.mockMvc.perform(get(String.format("/voucher/%s", UUID.randomUUID())))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNewVoucher() throws Exception {
        when(service.create(any(Voucher.class))).thenReturn(voucher);
        MockHttpServletRequestBuilder requestBuilder = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.discount").value(0.3))
                .andExpect(jsonPath("$.creationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expiryDate").value(expiryDate.format(formatter)));
    }


    @Test
    void shouldReturnEditedVoucher() throws Exception {
        when(service.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));
        voucher.setDiscount(0.5);
        voucher.setMinPurchase(100000.0);
        voucher.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        voucher.setExpiryDate(null);
        when(service.create(any(Voucher.class))).thenReturn(voucher);
        String jsonEdit = """
                {
                    "code": "%s",
                    "discount": 0.5,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": "BANK_TRANSFER",
                    "creationDate": "%s",
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, code, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").value(0.5))
                .andExpect(jsonPath("$.minPurchase").value(100000.0))
                .andExpect(jsonPath("$.paymentMethod").value("BANK_TRANSFER"));
    }

    @Test
    void editShouldReturnBadRequestIfCodeNull() throws Exception {
        String jsonEdit = """
                {
                    "code": null,
                    "discount": 0.5,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": "BANK_TRANSFER",
                    "creationDate": "%s",
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestIfDiscountNull() throws Exception {
        String jsonEdit = """
                {
                    "code": "%s",
                    "discount": null,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": "BANK_TRANSFER",
                    "creationDate": "%s",
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, code, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestIfCreationDateNull() throws Exception {
        String jsonEdit = """
                {
                    "code": "%s",
                    "discount": 0.5,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": "BANK_TRANSFER",
                    "creationDate": null,
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, code, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
    @Test
    void editShouldReturnBadRequestIfPaymentMethodNull() throws Exception {
        String jsonEdit = """
                {
                    "code": "%s",
                    "discount": 0.5,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": null,
                    "creationDate": "%s",
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, code, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }
    @Test
    void editShouldReturnBadRequestIfCodeNotFound() throws Exception {
        String jsonEdit = """
                {
                    "code": "%s",
                    "discount": 0.5,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": "BANK_TRANSFER",
                    "creationDate": "%s",
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, UUID.randomUUID(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        when(service.findByCode(any(UUID.class))).thenReturn(CompletableFuture.completedFuture(null));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDeletedVoucher() throws Exception {
        when(service.deleteByCode(code)).thenReturn(voucher);
        this.mockMvc.perform(delete(String.format("/voucher/delete/%s", code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnAllDeletedVouchers() throws Exception {
        when(service.findAll()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(voucher)));
        MvcResult mvcResult = this.mockMvc.perform(delete("/voucher/deleteAll"))
                .andExpect(request().asyncStarted())
                .andReturn();
        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value(code.toString()));
    }

    @Test
    void shouldReturnNotFoundIfDeleteNotFound() throws Exception {
        this.mockMvc.perform(delete(String.format("/voucher/delete/%s", UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }
}
