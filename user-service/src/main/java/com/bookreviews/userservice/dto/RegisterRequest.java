package com.bookreviews.userservice.dto;

import com.bookreviews.userservice.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Cambiamos Role por Set<Role>
    // Usamos @NotEmpty porque una colección no debe estar vacía
    @NotEmpty
    private Set<Role> roles;
}
