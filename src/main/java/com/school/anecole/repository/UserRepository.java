package com.school.anecole.repository;

import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.enabled = true")
    List<User> findActiveUsersByRole(@Param("role") UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT' AND u.grade = :grade")
    List<User> findStudentsByGrade(@Param("grade") Integer grade);
    
    @Query("SELECT u FROM User u WHERE u.role = 'TEACHER' AND u.subject = :subject")
    List<User> findTeachersBySubject(@Param("subject") String subject);
    
    // Новые методы для UserService
    
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String username, String email, String firstName, String lastName);
    
    Long countByEnabledTrue();
    
    Long countByRole(UserRole role);
    
    List<User> findByGrade(Integer grade);
    
    List<User> findBySchoolContainingIgnoreCase(String school);
    
    List<User> findBySubjectContainingIgnoreCase(String subject);
    
    List<User> findByEnabledTrue();
    
    List<User> findByEnabledFalse();
    
    List<User> findByEmailVerifiedTrue();
    
    List<User> findByEmailVerifiedFalse();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'STUDENT' AND u.grade = :grade")
    Long countStudentsByGrade(@Param("grade") Integer grade);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'TEACHER' AND u.subject = :subject")
    Long countTeachersBySubject(@Param("subject") String subject);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.school = :school")
    Long countUsersBySchool(@Param("school") String school);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate")
    List<User> findUsersCreatedAfter(@Param("startDate") java.time.LocalDateTime startDate);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :startDate")
    List<User> findUsersLoggedInAfter(@Param("startDate") java.time.LocalDateTime startDate);
} 