package com.bookreviews.userservice.controller;

import com.bookreviews.userservice.dto.AuthResponse;
import com.bookreviews.userservice.dto.LoginRequest;
import com.bookreviews.userservice.dto.RegisterRequest;
import com.bookreviews.userservice.model.Role;
import com.bookreviews.userservice.model.User;
import com.bookreviews.userservice.repository.UserRepository;
import com.bookreviews.userservice.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleStatus;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // -------------------------
    // REGISTER
    // -------------------------
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
                null,
                request.getName(),
                request.getEmail(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Role.ROLE_READER // 👈 forzado
        );

        userRepository.save(user);

        List<String> roles = List.of(user.getRole().name());

        String token = jwtService.generateToken(user.getUsername(), roles);

        return new AuthResponse(token);
    }

    // -------------------------
    // LOGIN
    // -------------------------
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtService.generateToken(auth.getName(), roles);

        return new AuthResponse(token);
    }
}
