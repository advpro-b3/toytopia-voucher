package id.ac.ui.cs.advprog.toytopiavoucher.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class UserServiceImplTest {
    @Value("${auth-api}")
    private String authApi;
    private String adminToken;
    private String userToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        String jsonAdmin = """
                {
                    "username": "admin",
                    "password": "AdproMatkulPalingKerenBanget1234"
                }
                """;
        String jsonUser = """
                {
                    "username": "hanauballs",
                    "password": "1234"
                }
                """;
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> adminResponse = restTemplate.getForObject(
                authApi + "/authenticate",
                Map.class
        );
    }
}
