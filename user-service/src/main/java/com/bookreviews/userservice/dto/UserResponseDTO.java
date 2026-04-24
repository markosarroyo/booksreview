package com.bookreviews.userservice.dto;

import com.bookreviews.userservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String username;
    private Set<Role> roles;
}
