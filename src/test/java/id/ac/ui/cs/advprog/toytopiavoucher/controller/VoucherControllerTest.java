package id.ac.ui.cs.advprog.toytopiavoucher.controller;

import com.jayway.jsonpath.JsonPath;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoucherController.class)
@ActiveProfiles("test")
public class VoucherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VoucherService service;
    private UUID code;
    private String json;
    private String expiryDate;

    @BeforeEach
    void setUp() throws Exception {
        expiryDate = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        json = """
                {
                    "discount": 0.3,
                    "minPurchase": 150000.0,
                    "expiryDate": "%s",
                    "paymentMethod": "CREDIT_CARD"
                }""";
        json = String.format(json, expiryDate);
        MockHttpServletRequestBuilder requestBuilder = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        code = UUID.fromString(JsonPath.parse(response).read("$.code"));
    }

    @AfterEach
    void tearDown() throws Exception {
        this.mockMvc.perform(delete("/voucher/deleteAll"));
    }

    @Test
    void shouldReturnOneVoucher() throws Exception {
        this.mockMvc.perform(get("/voucher/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value(code.toString()));
    }

    @Test
    void shouldReturnSelectedVoucher() throws Exception {
        this.mockMvc.perform(get(String.format("/voucher/%s", code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnNotFoundIfVoucherNotExist() throws Exception {
        this.mockMvc.perform(get(String.format("/voucher/%s", UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNewVoucher() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/voucher/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.discount").value(0.3))
                .andExpect(jsonPath("$.creationDate").value(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.expiryDate").value(expiryDate));
    }


    @Test
    void shouldReturnEditedVoucher() throws Exception {
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


        MockHttpServletRequestBuilder requestBuilder = put("/voucher/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEdit);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDeletedVoucher() throws Exception {
        this.mockMvc.perform(delete(String.format("/voucher/delete/%s", code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code.toString()));
    }

    @Test
    void shouldReturnAllDeletedVouchers() throws Exception {
        this.mockMvc.perform(delete("/voucher/deleteAll"))
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
