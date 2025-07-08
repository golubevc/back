package com.school.anecole.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
    }

    @Test
    void testAuthRequestCreation() {
        // Arrange
        authRequest.setUsername("testuser");
        authRequest.setPassword("password123");

        // Assert
        assertEquals("testuser", authRequest.getUsername());
        assertEquals("password123", authRequest.getPassword());
    }

    @Test
    void testAuthRequestSettersAndGetters() {
        // Act
        authRequest.setUsername("newuser");
        authRequest.setPassword("newpassword");

        // Assert
        assertEquals("newuser", authRequest.getUsername());
        assertEquals("newpassword", authRequest.getPassword());
    }

    @Test
    void testAuthRequestWithConstructor() {
        // Act
        AuthRequest constructorRequest = new AuthRequest("testuser", "password123");

        // Assert
        assertEquals("testuser", constructorRequest.getUsername());
        assertEquals("password123", constructorRequest.getPassword());
    }

    @Test
    void testAuthRequestEquals() {
        // Arrange
        AuthRequest request1 = new AuthRequest("user1", "pass1");
        AuthRequest request2 = new AuthRequest("user1", "pass1");
        AuthRequest request3 = new AuthRequest("user2", "pass1");

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testAuthRequestHashCode() {
        // Arrange
        AuthRequest request1 = new AuthRequest("user1", "pass1");
        AuthRequest request2 = new AuthRequest("user1", "pass1");

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testAuthRequestToString() {
        // Arrange
        authRequest.setUsername("testuser");
        authRequest.setPassword("password123");

        // Act
        String result = authRequest.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("password123"));
    }
} 