package com.school.anecole.dto;

import com.school.anecole.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
    }

    @Test
    void testRegisterRequestCreation() {
        // Arrange
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setRole(UserRole.STUDENT);
        registerRequest.setGrade(10);
        registerRequest.setSchool("Test School");

        // Assert
        assertEquals("testuser", registerRequest.getUsername());
        assertEquals("test@example.com", registerRequest.getEmail());
        assertEquals("password123", registerRequest.getPassword());
        assertEquals("John", registerRequest.getFirstName());
        assertEquals("Doe", registerRequest.getLastName());
        assertEquals(UserRole.STUDENT, registerRequest.getRole());
        assertEquals(10, registerRequest.getGrade());
        assertEquals("Test School", registerRequest.getSchool());
    }

    @Test
    void testRegisterRequestTeacherFields() {
        // Arrange
        registerRequest.setUsername("teacher");
        registerRequest.setEmail("teacher@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Smith");
        registerRequest.setRole(UserRole.TEACHER);
        registerRequest.setSubject("Mathematics");
        registerRequest.setQualification("PhD");

        // Assert
        assertEquals("teacher", registerRequest.getUsername());
        assertEquals("teacher@example.com", registerRequest.getEmail());
        assertEquals("password123", registerRequest.getPassword());
        assertEquals("Jane", registerRequest.getFirstName());
        assertEquals("Smith", registerRequest.getLastName());
        assertEquals(UserRole.TEACHER, registerRequest.getRole());
        assertEquals("Mathematics", registerRequest.getSubject());
        assertEquals("PhD", registerRequest.getQualification());
    }

    @Test
    void testRegisterRequestSettersAndGetters() {
        // Act
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("newpassword");
        registerRequest.setFirstName("New");
        registerRequest.setLastName("User");
        registerRequest.setRole(UserRole.STUDENT);
        registerRequest.setGrade(11);
        registerRequest.setSchool("New School");
        registerRequest.setSubject("Physics");
        registerRequest.setQualification("MSc");

        // Assert
        assertEquals("newuser", registerRequest.getUsername());
        assertEquals("new@example.com", registerRequest.getEmail());
        assertEquals("newpassword", registerRequest.getPassword());
        assertEquals("New", registerRequest.getFirstName());
        assertEquals("User", registerRequest.getLastName());
        assertEquals(UserRole.STUDENT, registerRequest.getRole());
        assertEquals(11, registerRequest.getGrade());
        assertEquals("New School", registerRequest.getSchool());
        assertEquals("Physics", registerRequest.getSubject());
        assertEquals("MSc", registerRequest.getQualification());
    }

    @Test
    void testRegisterRequestEquals() {
        // Arrange
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("user1");
        request1.setEmail("user1@example.com");
        request1.setPassword("pass1");
        request1.setFirstName("User");
        request1.setLastName("One");
        request1.setRole(UserRole.STUDENT);

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("user1");
        request2.setEmail("user1@example.com");
        request2.setPassword("pass1");
        request2.setFirstName("User");
        request2.setLastName("One");
        request2.setRole(UserRole.STUDENT);

        RegisterRequest request3 = new RegisterRequest();
        request3.setUsername("user2");
        request3.setEmail("user2@example.com");
        request3.setPassword("pass2");
        request3.setFirstName("User");
        request3.setLastName("Two");
        request3.setRole(UserRole.TEACHER);

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testRegisterRequestHashCode() {
        // Arrange
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("user1");
        request1.setEmail("user1@example.com");
        request1.setPassword("pass1");
        request1.setFirstName("User");
        request1.setLastName("One");
        request1.setRole(UserRole.STUDENT);

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("user1");
        request2.setEmail("user1@example.com");
        request2.setPassword("pass1");
        request2.setFirstName("User");
        request2.setLastName("One");
        request2.setRole(UserRole.STUDENT);

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testRegisterRequestToString() {
        // Arrange
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setRole(UserRole.STUDENT);

        // Act
        String result = registerRequest.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("STUDENT"));
    }
} 