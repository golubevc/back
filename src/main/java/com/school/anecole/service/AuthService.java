package com.school.anecole.service;

import com.school.anecole.dto.AuthRequest;
import com.school.anecole.dto.AuthResponse;
import com.school.anecole.dto.RegisterRequest;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.repository.UserRepository;
import com.school.anecole.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponse(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole()
        );
    }
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setGrade(request.getGrade());
        user.setSchool(request.getSchool());
        user.setSubject(request.getSubject());
        user.setQualification(request.getQualification());
        
        User savedUser = userRepository.save(user);
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getRole()
        );
    }
    
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
    
    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }
} 