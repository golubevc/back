package com.school.anecole.controller;

import com.school.anecole.model.User;
import com.school.anecole.service.UserService;
import com.school.anecole.dto.UserStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error getting user by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        try {
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting users by role {}: {}", role, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String term) {
        try {
            List<User> users = userService.searchUsers(term);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error searching users with term '{}': {}", term, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            logger.info("User created with id: {}", createdUser.getId());
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            logger.info("User updated with id: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Error updating user {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            logger.info("User deleted with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting user {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> enableUser(@PathVariable Long id) {
        try {
            User enabledUser = userService.enableUser(id);
            logger.info("User enabled with id: {}", id);
            return ResponseEntity.ok(enabledUser);
        } catch (Exception e) {
            logger.error("Error enabling user {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> disableUser(@PathVariable Long id) {
        try {
            User disabledUser = userService.disableUser(id);
            logger.info("User disabled with id: {}", id);
            return ResponseEntity.ok(disabledUser);
        } catch (Exception e) {
            logger.error("Error disabling user {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        try {
            User updatedUser = userService.updateUserRole(id, role);
            logger.info("User role updated for id {} to role: {}", id, role);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Error updating user role for id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserStats> getUserStats() {
        try {
            UserStats stats = userService.getUserStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting user stats: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/stats/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getActiveUsersCount() {
        try {
            Long count = userService.getActiveUsersCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting active users count: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/stats/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUsersCountByRole(@PathVariable String role) {
        try {
            Long count = userService.getUsersCountByRole(role);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting users count by role {}: {}", role, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 