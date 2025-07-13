package com.school.anecole.service;

import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.repository.UserRepository;
import com.school.anecole.dto.UserStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public List<User> getUsersByRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.findByRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
    
    public List<User> searchUsers(String term) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                term, term, term, term);
    }
    
    public User createUser(User user) {
        // Проверяем, что username и email уникальны
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Устанавливаем значения по умолчанию
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
        user.setEmailVerified(false);
        
        // Шифруем пароль, если он не зашифрован
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        
        // Проверяем уникальность username и email
        Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
        if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(user.getId())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(user.getId())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Обновляем поля
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setRole(user.getRole());
        existingUser.setGrade(user.getGrade());
        existingUser.setSchool(user.getSchool());
        existingUser.setSubject(user.getSubject());
        existingUser.setQualification(user.getQualification());
        existingUser.setAvatar(user.getAvatar());
        
        // Шифруем пароль, если он изменился
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    public User enableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        return userRepository.save(user);
    }
    
    public User disableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        return userRepository.save(user);
    }
    
    public User updateUserRole(Long id, String role) {
        User user = getUserById(id);
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            user.setRole(userRole);
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
    
    public UserStats getUserStats() {
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByEnabledTrue();
        Long students = userRepository.countByRole(UserRole.STUDENT);
        Long teachers = userRepository.countByRole(UserRole.TEACHER);
        Long admins = userRepository.countByRole(UserRole.ADMIN);
        
        return new UserStats(totalUsers, activeUsers, students, teachers, admins);
    }
    
    public Long getActiveUsersCount() {
        return userRepository.countByEnabledTrue();
    }
    
    public Long getUsersCountByRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.countByRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
    
    public List<User> getUsersByGrade(Integer grade) {
        return userRepository.findByGrade(grade);
    }
    
    public List<User> getUsersBySchool(String school) {
        return userRepository.findBySchoolContainingIgnoreCase(school);
    }
    
    public List<User> getUsersBySubject(String subject) {
        return userRepository.findBySubjectContainingIgnoreCase(subject);
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    public List<User> getEnabledUsers() {
        return userRepository.findByEnabledTrue();
    }
    
    public List<User> getDisabledUsers() {
        return userRepository.findByEnabledFalse();
    }
    
    public List<User> getVerifiedUsers() {
        return userRepository.findByEmailVerifiedTrue();
    }
    
    public User getCurrentUser() {
        // Получаем текущего пользователя из контекста безопасности
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        
        return getUserByUsername(username);
    }
    
    public List<User> getUnverifiedUsers() {
        return userRepository.findByEmailVerifiedFalse();
    }
} 