package com.bookreviews.userservice.controller;

import com.bookreviews.userservice.dto.AuthResponse;
import com.bookreviews.userservice.dto.LoginRequest;
import com.bookreviews.userservice.dto.RegisterRequest;
import com.bookreviews.userservice.model.User;
import com.bookreviews.userservice.security.JwtService;
import com.bookreviews.userservice.service.UserService;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        String token = jwtService.generateToken(user.getUsername(), roles);
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
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