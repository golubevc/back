package com.school.anecole.dto;

import com.school.anecole.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = new AuthResponse();
    }

    @Test
    void testAuthResponseCreation() {
        // Arrange
        authResponse.setToken("jwt-token");
        authResponse.setUsername("testuser");
        authResponse.setEmail("test@example.com");
        authResponse.setFirstName("John");
        authResponse.setLastName("Doe");
        authResponse.setRole(UserRole.STUDENT);

        // Assert
        assertEquals("jwt-token", authResponse.getToken());
        assertEquals("testuser", authResponse.getUsername());
        assertEquals("test@example.com", authResponse.getEmail());
        assertEquals("John", authResponse.getFirstName());
        assertEquals("Doe", authResponse.getLastName());
        assertEquals(UserRole.STUDENT, authResponse.getRole());
    }

    @Test
    void testAuthResponseWithConstructor() {
        // Act
        AuthResponse constructorResponse = new AuthResponse(
            "jwt-token",
            "testuser",
            "test@example.com",
            "John",
            "Doe",
            UserRole.STUDENT
        );

        // Assert
        assertEquals("jwt-token", constructorResponse.getToken());
        assertEquals("testuser", constructorResponse.getUsername());
        assertEquals("test@example.com", constructorResponse.getEmail());
        assertEquals("John", constructorResponse.getFirstName());
        assertEquals("Doe", constructorResponse.getLastName());
        assertEquals(UserRole.STUDENT, constructorResponse.getRole());
    }

    @Test
    void testAuthResponseSettersAndGetters() {
        // Act
        authResponse.setToken("new-token");
        authResponse.setUsername("newuser");
        authResponse.setEmail("new@example.com");
        authResponse.setFirstName("Jane");
        authResponse.setLastName("Smith");
        authResponse.setRole(UserRole.TEACHER);

        // Assert
        assertEquals("new-token", authResponse.getToken());
        assertEquals("newuser", authResponse.getUsername());
        assertEquals("new@example.com", authResponse.getEmail());
        assertEquals("Jane", authResponse.getFirstName());
        assertEquals("Smith", authResponse.getLastName());
        assertEquals(UserRole.TEACHER, authResponse.getRole());
    }

    @Test
    void testAuthResponseEquals() {
        // Arrange
        AuthResponse response1 = new AuthResponse("token1", "user1", "user1@example.com", "User", "One", UserRole.STUDENT);
        AuthResponse response2 = new AuthResponse("token1", "user1", "user1@example.com", "User", "One", UserRole.STUDENT);
        AuthResponse response3 = new AuthResponse("token2", "user2", "user2@example.com", "User", "Two", UserRole.TEACHER);

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void testAuthResponseHashCode() {
        // Arrange
        AuthResponse response1 = new AuthResponse("token1", "user1", "user1@example.com", "User", "One", UserRole.STUDENT);
        AuthResponse response2 = new AuthResponse("token1", "user1", "user1@example.com", "User", "One", UserRole.STUDENT);

        // Assert
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testAuthResponseToString() {
        // Arrange
        authResponse.setToken("jwt-token");
        authResponse.setUsername("testuser");
        authResponse.setEmail("test@example.com");
        authResponse.setFirstName("John");
        authResponse.setLastName("Doe");
        authResponse.setRole(UserRole.STUDENT);

        // Act
        String result = authResponse.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("jwt-token"));
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("STUDENT"));
    }

    @Test
    void testAuthResponseWithNullValues() {
        // Act
        authResponse.setToken(null);
        authResponse.setUsername(null);
        authResponse.setEmail(null);
        authResponse.setFirstName(null);
        authResponse.setLastName(null);
        authResponse.setRole(null);

        // Assert
        assertNull(authResponse.getToken());
        assertNull(authResponse.getUsername());
        assertNull(authResponse.getEmail());
        assertNull(authResponse.getFirstName());
        assertNull(authResponse.getLastName());
        assertNull(authResponse.getRole());
    }

    @Test
    void testAuthResponseWithEmptyValues() {
        // Act
        authResponse.setToken("");
        authResponse.setUsername("");
        authResponse.setEmail("");
        authResponse.setFirstName("");
        authResponse.setLastName("");

        // Assert
        assertEquals("", authResponse.getToken());
        assertEquals("", authResponse.getUsername());
        assertEquals("", authResponse.getEmail());
        assertEquals("", authResponse.getFirstName());
        assertEquals("", authResponse.getLastName());
    }
} 