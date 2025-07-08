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
public class StudentService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private StudentProgressRepository progressRepository;
    
    public List<User> getAllStudents() {
        return userRepository.findByRole(UserRole.STUDENT);
    }
    
    public List<User> getStudentsByGrade(Integer grade) {
        return userRepository.findStudentsByGrade(grade);
    }
    
    public Optional<User> getStudentById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<Class> getStudentClasses(Long studentId) {
        return classRepository.findByStudentId(studentId);
    }
    
    public List<Lesson> getStudentLessons(Long studentId) {
        return lessonRepository.findActiveByStudentId(studentId);
    }
    
    public List<StudentProgress> getStudentProgress(Long studentId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return progressRepository.findByStudent(student);
    }
    
    public List<StudentProgress> getStudentProgressByLesson(Long studentId, Long lessonId) {
        return progressRepository.findByStudentIdAndLessonId(studentId, lessonId);
    }
    
    public StudentProgress updateProgress(Long studentId, Long lessonId, Integer score) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        
        // Find or create progress record
        List<StudentProgress> existingProgress = progressRepository.findByStudentIdAndLessonId(studentId, lessonId);
        StudentProgress progress;
        
        if (existingProgress.isEmpty()) {
            // Create new progress record
            progress = new StudentProgress();
            progress.setStudent(student);
            progress.setLesson(lesson);
        } else {
            progress = existingProgress.get(0);
        }
        
        progress.incrementAttempts();
        progress.updateScore(score);
        
        if (score >= 80) { // Threshold for completion
            progress.setCompleted(true);
        }
        
        return progressRepository.save(progress);
    }
    
    public Long getCompletedLessonsCount(Long studentId) {
        return progressRepository.countCompletedByStudentId(studentId);
    }
    
    public Double getAverageScore(Long studentId) {
        return progressRepository.getAverageScoreByStudentId(studentId);
    }
    
    public List<StudentProgress> getCompletedProgress(Long studentId) {
        return progressRepository.findCompletedByStudentId(studentId);
    }
} 