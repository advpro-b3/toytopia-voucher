package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import id.ac.ui.cs.advprog.toytopiavoucher.enums.PaymentMethod;
import id.ac.ui.cs.advprog.toytopiavoucher.model.Voucher;
import id.ac.ui.cs.advprog.toytopiavoucher.service.UserService;
import id.ac.ui.cs.advprog.toytopiavoucher.service.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoucherController.class)
class VoucherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VoucherService voucherService;
    @MockBean
    private UserService userService;
    private Voucher voucher;
    private UUID code;
    private String json;
    private LocalDate expiryDate;
    private DateTimeFormatter formatter;
    private String token;

    @BeforeEach
    void setUp() {
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
        token = "Bearer eyu45320u";
    }

    @Test
    void shouldReturnOneVoucher() throws Exception {
        when(voucherService.findAll()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(voucher)));

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
        when(voucherService.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));

        MvcResult mvcResult = this.mockMvc.perform(get(String.format("/voucher/%s", code)))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnNotFoundIfVoucherNotExist() throws Exception {
        when(voucherService.findByCode(any(UUID.class))).thenReturn(CompletableFuture.completedFuture(null));

        MvcResult mvcResult = this.mockMvc.perform(get(String.format("/voucher/%s", UUID.randomUUID())))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNewVoucher() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        when(voucherService.create(any(Voucher.class))).thenReturn(voucher);
        MockHttpServletRequestBuilder requestBuilder = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(json);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.discount").value(0.3))
                .andExpect(jsonPath("$.creationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expiryDate").value(expiryDate.format(formatter)));
    }

    @Test
    void shouldReturnEditedVoucher() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        when(voucherService.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));
        voucher.setDiscount(0.5);
        voucher.setMinPurchase(100000.0);
        voucher.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        voucher.setExpiryDate(null);
        when(voucherService.create(any(Voucher.class))).thenReturn(voucher);
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
                .header("Authorization", token)
                .content(jsonEdit);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").value(0.5))
                .andExpect(jsonPath("$.minPurchase").value(100000.0))
                .andExpect(jsonPath("$.paymentMethod").value("BANK_TRANSFER"));
    }

    void testEditInvalidPaths(String code, String discount, String paymentMethod, String creationDate) throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        String jsonEdit = """
                {
                    "code": %s,
                    "discount": %s,
                    "maxDiscount": null,
                    "minPurchase": 100000.0,
                    "paymentMethod": %s,
                    "creationDate": %s,
                    "expiryDate": null
                }""";
        jsonEdit = String.format(jsonEdit, code, discount, paymentMethod, creationDate);

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(jsonEdit);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());

    }

    @Test
    void editShouldReturnBadRequestIfCodeNull() throws Exception {
        testEditInvalidPaths(
                null,
                "\"0.5\"",
                "\"BANK_TRANSFER\"",
                "\"" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\""
                );
    }

    @Test
    void editShouldReturnBadRequestIfDiscountNull() throws Exception {
        testEditInvalidPaths(
                "\"" + code + "\"",
                null,
                "\"BANK_TRANSFER\"",
                "\"" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\""
        );
    }

    @Test
    void editShouldReturnBadRequestIfCreationDateNull() throws Exception {
        testEditInvalidPaths(
                "\"" + code + "\"",
                "\"0.5\"",
                "\"BANK_TRANSFER\"",
                null
        );
    }
    @Test
    void editShouldReturnBadRequestIfPaymentMethodNull() throws Exception {
        testEditInvalidPaths(
                "\"" + code + "\"",
                "\"0.5\"",
                null,
                "\"" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\""
        );
    }
    @Test
    void editShouldReturnBadRequestIfCodeNotFound() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
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

        when(voucherService.findByCode(any(UUID.class))).thenReturn(CompletableFuture.completedFuture(null));

        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(jsonEdit);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDeletedVoucher() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        when(voucherService.deleteByCode(code)).thenReturn(voucher);

        MvcResult mvcResult = this.mockMvc.perform(delete(String.format("/voucher/delete/%s", code))
                        .header("Authorization", token))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnAllDeletedVouchers() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        when(voucherService.findAll()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(voucher)));
        MvcResult mvcResult = this.mockMvc.perform(delete("/voucher/deleteAll")
                        .header("Authorization", token))
                .andExpect(request().asyncStarted())
                .andReturn();
        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value(code.toString()));
    }

    @Test
    void shouldReturnNotFoundIfDeleteNotFound() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(true));
        when(voucherService.deleteByCode(any(UUID.class))).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = delete(String.format("/voucher/delete/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestIfInvalidToken() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.failedFuture(new Exception()));
        when(voucherService.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));
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

        MockHttpServletRequestBuilder createRequest = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(json);

        MockHttpServletRequestBuilder editRequest = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(jsonEdit);

        MockHttpServletRequestBuilder deleteRequest = delete(String.format("/voucher/delete/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        MockHttpServletRequestBuilder deleteAllRequest = delete("/voucher/deleteAll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        MvcResult createResult = this.mockMvc.perform(createRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult editResult = this.mockMvc.perform(editRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult deleteResult = this.mockMvc.perform(deleteRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult deleteAllResult = this.mockMvc.perform(deleteAllRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(createResult)).andExpect(status().isBadRequest());
        this.mockMvc.perform(asyncDispatch(editResult)).andExpect(status().isBadRequest());
        this.mockMvc.perform(asyncDispatch(deleteResult)).andExpect(status().isBadRequest());
        this.mockMvc.perform(asyncDispatch(deleteAllResult)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnForbiddenIfNotAdmin() throws Exception {
        when(userService.isAdmin(token)).thenReturn(CompletableFuture.completedFuture(false));
        when(voucherService.findByCode(code)).thenReturn(CompletableFuture.completedFuture(voucher));
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

        MockHttpServletRequestBuilder createRequest = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(json);

        MockHttpServletRequestBuilder editRequest = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(jsonEdit);

        MockHttpServletRequestBuilder deleteRequest = delete(String.format("/voucher/delete/%s", UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        MockHttpServletRequestBuilder deleteAllRequest = delete("/voucher/deleteAll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token);

        MvcResult createResult = this.mockMvc.perform(createRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult editResult = this.mockMvc.perform(editRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult deleteResult = this.mockMvc.perform(deleteRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult deleteAllResult = this.mockMvc.perform(deleteAllRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(createResult)).andExpect(status().isForbidden());
        this.mockMvc.perform(asyncDispatch(editResult)).andExpect(status().isForbidden());
        this.mockMvc.perform(asyncDispatch(deleteResult)).andExpect(status().isForbidden());
        this.mockMvc.perform(asyncDispatch(deleteAllResult)).andExpect(status().isForbidden());
    }
}
