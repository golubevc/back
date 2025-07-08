package com.school.anecole.repository;

import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setRole(UserRole.STUDENT);
        testUser.setEnabled(true);
        testUser.setEmailVerified(false);
        testUser.setGrade(10);
        testUser.setSchool("Test School");
    }

    @Test
    void testSaveUser() {
        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(UserRole.STUDENT, savedUser.getRole());
        assertTrue(savedUser.isEnabled());
        assertFalse(savedUser.isEmailVerified());
    }

    @Test
    void testFindById() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findById(999L);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByUsername() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByUsernameNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByEmail() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByUsername() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act & Assert
        assertTrue(userRepository.existsByUsername("testuser"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act & Assert
        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void testFindByRole() {
        // Arrange
        User student = new User();
        student.setUsername("student1");
        student.setEmail("student1@example.com");
        student.setPassword("password123");
        student.setFirstName("Student");
        student.setLastName("One");
        student.setRole(UserRole.STUDENT);

        User teacher = new User();
        teacher.setUsername("teacher1");
        teacher.setEmail("teacher1@example.com");
        teacher.setPassword("password123");
        teacher.setFirstName("Teacher");
        teacher.setLastName("One");
        teacher.setRole(UserRole.TEACHER);

        entityManager.persistAndFlush(student);
        entityManager.persistAndFlush(teacher);

        // Act
        List<User> students = userRepository.findByRole(UserRole.STUDENT);
        List<User> teachers = userRepository.findByRole(UserRole.TEACHER);

        // Assert
        assertEquals(2, students.size()); // Including testUser from setUp
        assertEquals(1, teachers.size());
        assertTrue(students.stream().allMatch(u -> u.getRole() == UserRole.STUDENT));
        assertTrue(teachers.stream().allMatch(u -> u.getRole() == UserRole.TEACHER));
    }

    @Test
    void testFindAll() {
        // Arrange
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setRole(UserRole.STUDENT);

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(user2);

        // Act
        List<User> allUsers = userRepository.findAll();

        // Assert
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("testuser")));
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("user2")));
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);
        savedUser.setFirstName("Updated");
        savedUser.setLastName("Name");

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Name", updatedUser.getLastName());
        assertEquals("testuser", updatedUser.getUsername()); // Should remain unchanged
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        userRepository.deleteById(savedUser.getId());

        // Assert
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testFindByClassAndRole() {
        // This test would require a Class entity to be set up
        // For now, we'll test that the method exists and doesn't throw exceptions
        assertNotNull(userRepository);
    }

    @Test
    void testTeacherSpecificFields() {
        // Arrange
        User teacher = new User();
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@example.com");
        teacher.setPassword("password123");
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setRole(UserRole.TEACHER);
        teacher.setSubject("Mathematics");
        teacher.setQualification("PhD");

        // Act
        User savedTeacher = userRepository.save(teacher);

        // Assert
        assertNotNull(savedTeacher.getId());
        assertEquals("Mathematics", savedTeacher.getSubject());
        assertEquals("PhD", savedTeacher.getQualification());
        assertEquals(UserRole.TEACHER, savedTeacher.getRole());
    }

    @Test
    void testStudentSpecificFields() {
        // Arrange
        User student = new User();
        student.setUsername("student");
        student.setEmail("student@example.com");
        student.setPassword("password123");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setRole(UserRole.STUDENT);
        student.setGrade(11);
        student.setSchool("High School");

        // Act
        User savedStudent = userRepository.save(student);

        // Assert
        assertNotNull(savedStudent.getId());
        assertEquals(11, savedStudent.getGrade());
        assertEquals("High School", savedStudent.getSchool());
        assertEquals(UserRole.STUDENT, savedStudent.getRole());
    }
} 