package id.ac.ui.cs.advprog.toytopiavoucher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String role;
}
