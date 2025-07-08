package com.school.anecole.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.anecole.dto.AuthRequest;
import com.school.anecole.dto.RegisterRequest;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        // Clear database before each test
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(UserRole.STUDENT);
        request.setGrade(10);
        request.setSchool("Test School");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.token").exists());

        // Verify user was saved in database
        User savedUser = userRepository.findByUsername("newuser").orElse(null);
        assert savedUser != null;
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("new@example.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    void testRegisterDuplicateUsername() throws Exception {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setRole(UserRole.STUDENT);
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(UserRole.STUDENT);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterDuplicateEmail() throws Exception {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setRole(UserRole.STUDENT);
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(UserRole.STUDENT);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setUsername("nonexistent");
        request.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testValidateToken() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Login to get token
        String loginResponse = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        // Act & Assert - Validate token
        mockMvc.perform(post("/auth/validate")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testValidateInvalidToken() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/validate")
                .param("token", "invalid.token.here"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testRegisterTeacher() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("teacher");
        request.setEmail("teacher@example.com");
        request.setPassword("password123");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setRole(UserRole.TEACHER);
        request.setSubject("Mathematics");
        request.setQualification("PhD");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("teacher"))
                .andExpect(jsonPath("$.role").value("TEACHER"))
                .andExpect(jsonPath("$.token").exists());

        // Verify teacher was saved with teacher-specific fields
        User savedTeacher = userRepository.findByUsername("teacher").orElse(null);
        assert savedTeacher != null;
        assertEquals("Mathematics", savedTeacher.getSubject());
        assertEquals("PhD", savedTeacher.getQualification());
    }
} 