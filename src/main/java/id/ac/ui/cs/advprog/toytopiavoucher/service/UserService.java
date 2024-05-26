package id.ac.ui.cs.advprog.toytopiavoucher.service;

import id.ac.ui.cs.advprog.toytopiavoucher.dto.UserResponse;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<Boolean> isAdmin(String token);
}
