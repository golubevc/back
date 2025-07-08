package com.school.anecole.controller;

import com.school.anecole.dto.AuthRequest;
import com.school.anecole.dto.AuthResponse;
import com.school.anecole.dto.RegisterRequest;
import com.school.anecole.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            logger.info("Login attempt for username: {}", request.getUsername());
            AuthResponse response = authService.login(request);
            logger.info("Login successful for username: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for username: {}, error: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Registration attempt for username: {}", request.getUsername());
            AuthResponse response = authService.register(request);
            logger.info("Registration successful for username: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for username: {}, error: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenValidationRequest request) {
        try {
            boolean isValid = authService.validateToken(request.getToken());
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage(), e);
            return ResponseEntity.ok(false);
        }
    }
    
    // Внутренний класс для запроса валидации токена
    public static class TokenValidationRequest {
        private String token;
        
        public String getToken() {
            return token;
        }
        
        public void setToken(String token) {
            this.token = token;
        }
    }
} 