package com.school.anecole.service;

import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.StudentProgress;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.StudentProgressRepository;
import com.school.anecole.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private StudentProgressRepository progressRepository;
    
    public List<User> getAllTeachers() {
        return userRepository.findByRole(UserRole.TEACHER);
    }
    
    public List<User> getTeachersBySubject(String subject) {
        return userRepository.findTeachersBySubject(subject);
    }
    
    public Optional<User> getTeacherById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<Class> getTeacherClasses(Long teacherId) {
        return classRepository.findActiveByTeacherId(teacherId);
    }
    
    public List<Lesson> getTeacherLessons(Long teacherId) {
        return lessonRepository.findActiveByTeacherId(teacherId);
    }
    
    public Class createClass(Class classEntity) {
        return classRepository.save(classEntity);
    }
    
    public Class updateClass(Class classEntity) {
        return classRepository.save(classEntity);
    }
    
    public void deleteClass(Long classId) {
        Class classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        classEntity.setActive(false);
        classRepository.save(classEntity);
    }
    
    public Class addStudentToClass(Long classId, Long studentId) {
        Class classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        classEntity.addStudent(student);
        return classRepository.save(classEntity);
    }
    
    public Class removeStudentFromClass(Long classId, Long studentId) {
        Class classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        classEntity.removeStudent(student);
        return classRepository.save(classEntity);
    }
    
    public List<StudentProgress> getClassProgress(Long classId) {
        // Get all students in the class and their progress
        Class classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        
        // This would need to be implemented based on your specific requirements
        // For now, returning empty list
        return List.of();
    }
    
    public List<StudentProgress> getLessonProgress(Long lessonId) {
        return progressRepository.findByLessonIdOrderByScoreDesc(lessonId);
    }
    
    public Double getClassAverageScore(Long classId) {
        // Calculate average score for all students in the class
        // This would need to be implemented based on your specific requirements
        return 0.0;
    }
} 