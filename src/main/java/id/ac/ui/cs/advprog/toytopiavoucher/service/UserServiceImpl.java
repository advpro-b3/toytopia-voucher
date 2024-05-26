package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {
    @Value("${auth-api}")
    private String AUTH_API;

    private CompletableFuture<UserResponse> getUsernameWithToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        try {
            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    AUTH_API + "/user", HttpMethod.GET, httpEntity, UserResponse.class);
            return CompletableFuture.completedFuture(response.getBody());
        } catch (HttpClientErrorException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<Boolean> isAdmin(String token) {
        CompletableFuture<UserResponse> userResponse = getUsernameWithToken(token);
        return userResponse.thenApply((result) -> result.getRole().equals("ADMIN"));
    }
}
