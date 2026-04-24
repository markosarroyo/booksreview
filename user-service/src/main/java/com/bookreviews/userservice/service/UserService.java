package com.bookreviews.userservice.service;

import com.bookreviews.userservice.dto.RegisterRequest;
import com.bookreviews.userservice.dto.UserResponseDTO;
import com.bookreviews.userservice.model.Role;
import com.bookreviews.userservice.model.User;
import com.bookreviews.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Set<Role> assignedRoles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of(Role.READER)
                : request.getRoles();

        User user = new User(
                null,
                request.getName(),
                request.getEmail(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                assignedRoles
        );

        return userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles()
        );
    }
}