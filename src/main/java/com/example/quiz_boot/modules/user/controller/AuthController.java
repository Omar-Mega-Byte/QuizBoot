package com.example.quiz_boot.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_boot.modules.user.dto.request.LoginRequestDto;
import com.example.quiz_boot.modules.user.dto.request.UserCreateDto;
import com.example.quiz_boot.modules.user.dto.response.JwtResponseDto;
import com.example.quiz_boot.modules.user.service.AuthService;

import jakarta.validation.Valid;

/**
 * REST controller for authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> registerUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        JwtResponseDto response = authService.registerUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate user and return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        JwtResponseDto response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
