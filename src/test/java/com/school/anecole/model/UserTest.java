package com.school.anecole.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.STUDENT);
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(UserRole.STUDENT, user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testUserRole() {
        user.setRole(UserRole.TEACHER);
        assertEquals(UserRole.TEACHER, user.getRole());
        
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testUserUpdate() {
        user.setUsername("updateduser");
        user.setEmail("updated@example.com");
        user.setPassword("newpassword");
        
        assertEquals("updateduser", user.getUsername());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testUserEquality() {
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        user2.setPassword("password");
        user2.setRole(UserRole.STUDENT);
        
        assertEquals(user.getId(), user2.getId());
        assertEquals(user.getUsername(), user2.getUsername());
        assertEquals(user.getEmail(), user2.getEmail());
    }

    @Test
    void testUserInequality() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("differentuser");
        user2.setEmail("different@example.com");
        user2.setPassword("differentpassword");
        user2.setRole(UserRole.TEACHER);
        
        assertNotEquals(user.getId(), user2.getId());
        assertNotEquals(user.getUsername(), user2.getUsername());
        assertNotEquals(user.getEmail(), user2.getEmail());
        assertNotEquals(user.getRole(), user2.getRole());
    }

    @Test
    void testUserToString() {
        String userString = user.toString();
        assertNotNull(userString);
        assertTrue(userString.contains("testuser"));
        assertTrue(userString.contains("test@example.com"));
    }

    @Test
    void testUserHashCode() {
        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testUserWithNullValues() {
        User nullUser = new User();
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getPassword());
        assertNull(nullUser.getRole());
        assertNull(nullUser.getCreatedAt());
    }
} 