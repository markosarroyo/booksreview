package com.bookreviews.userservice.dto;

import com.bookreviews.userservice.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank(message = "Please inform user name")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Please inform email")
    @Email(message = "Incorrect format for email")
    @Size(max = 100, message = "Email too long")
    private String email;

    @NotBlank(message = "Not empty username allowed")
    @Size(min = 3, max = 10, message = "Username must be between 3 and 10 characters")
    private String username;

    @NotBlank(message = "Not empty password allowed")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    // Cambiamos Role por Set<Role>
    // Usamos @NotEmpty porque una colección no debe estar vacía
    @NotEmpty
    private Set<Role> roles;
}
