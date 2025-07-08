package com.school.anecole.controller;

import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.StudentProgress;
import com.school.anecole.model.User;
import com.school.anecole.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<User>> getStudentsByGrade(@PathVariable Integer grade) {
        List<User> students = studentService.getStudentsByGrade(grade);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/classes")
    public ResponseEntity<List<Class>> getStudentClasses(@PathVariable Long id) {
        List<Class> classes = studentService.getStudentClasses(id);
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<Lesson>> getStudentLessons(@PathVariable Long id) {
        List<Lesson> lessons = studentService.getStudentLessons(id);
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/{id}/progress")
    public ResponseEntity<List<StudentProgress>> getStudentProgress(@PathVariable Long id) {
        List<StudentProgress> progress = studentService.getStudentProgress(id);
        return ResponseEntity.ok(progress);
    }
    
    @GetMapping("/{studentId}/progress/lesson/{lessonId}")
    public ResponseEntity<List<StudentProgress>> getStudentProgressByLesson(
            @PathVariable Long studentId, 
            @PathVariable Long lessonId) {
        List<StudentProgress> progress = studentService.getStudentProgressByLesson(studentId, lessonId);
        return ResponseEntity.ok(progress);
    }
    
    @PostMapping("/{studentId}/progress/word/{wordId}")
    public ResponseEntity<StudentProgress> updateProgress(
            @PathVariable Long studentId,
            @PathVariable Long wordId,
            @RequestParam Integer score) {
        StudentProgress progress = studentService.updateProgress(studentId, wordId, score);
        return ResponseEntity.ok(progress);
    }
    
    @GetMapping("/{id}/stats/completed")
    public ResponseEntity<Long> getCompletedLessonsCount(@PathVariable Long id) {
        Long count = studentService.getCompletedLessonsCount(id);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{id}/stats/average")
    public ResponseEntity<Double> getAverageScore(@PathVariable Long id) {
        Double average = studentService.getAverageScore(id);
        return ResponseEntity.ok(average);
    }
    
    @GetMapping("/{id}/progress/completed")
    public ResponseEntity<List<StudentProgress>> getCompletedProgress(@PathVariable Long id) {
        List<StudentProgress> progress = studentService.getCompletedProgress(id);
        return ResponseEntity.ok(progress);
    }
} 