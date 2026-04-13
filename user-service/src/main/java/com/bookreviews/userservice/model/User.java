package com.bookreviews.userservice.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    @NotBlank(message = "Please inform user name")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Please inform email")
    @Email(message = "Incorrect format for email")
    @Size(max = 100, message = "Email too long")
    @Indexed(unique = true)
    private String email;
    @NotBlank (message = "Not empty password allowed")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
    @NotBlank(message="Role empty")
    @Pattern(regexp="ADMIN|AUTOR|LECTOR", message = "Roles allowed ADMIN, AUTOR, LECTOR")
    private String role;
}

