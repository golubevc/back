package com.school.anecole.controller;

import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.StudentProgress;
import com.school.anecole.model.User;
import com.school.anecole.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllTeachers() {
        List<User> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<User>> getTeachersBySubject(@PathVariable String subject) {
        List<User> teachers = teacherService.getTeachersBySubject(subject);
        return ResponseEntity.ok(teachers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/classes")
    public ResponseEntity<List<Class>> getTeacherClasses(@PathVariable Long id) {
        List<Class> classes = teacherService.getTeacherClasses(id);
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<Lesson>> getTeacherLessons(@PathVariable Long id) {
        List<Lesson> lessons = teacherService.getTeacherLessons(id);
        return ResponseEntity.ok(lessons);
    }
    
    @PostMapping("/classes")
    public ResponseEntity<Class> createClass(@RequestBody Class classEntity) {
        Class createdClass = teacherService.createClass(classEntity);
        return ResponseEntity.ok(createdClass);
    }
    
    @PutMapping("/classes/{id}")
    public ResponseEntity<Class> updateClass(@PathVariable Long id, @RequestBody Class classEntity) {
        classEntity.setId(id);
        Class updatedClass = teacherService.updateClass(classEntity);
        return ResponseEntity.ok(updatedClass);
    }
    
    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        teacherService.deleteClass(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/classes/{classId}/students/{studentId}")
    public ResponseEntity<Class> addStudentToClass(
            @PathVariable Long classId,
            @PathVariable Long studentId) {
        Class updatedClass = teacherService.addStudentToClass(classId, studentId);
        return ResponseEntity.ok(updatedClass);
    }
    
    @DeleteMapping("/classes/{classId}/students/{studentId}")
    public ResponseEntity<Class> removeStudentFromClass(
            @PathVariable Long classId,
            @PathVariable Long studentId) {
        Class updatedClass = teacherService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok(updatedClass);
    }
    
    @GetMapping("/classes/{classId}/progress")
    public ResponseEntity<List<StudentProgress>> getClassProgress(@PathVariable Long classId) {
        List<StudentProgress> progress = teacherService.getClassProgress(classId);
        return ResponseEntity.ok(progress);
    }
    
    @GetMapping("/lessons/{lessonId}/progress")
    public ResponseEntity<List<StudentProgress>> getLessonProgress(@PathVariable Long lessonId) {
        List<StudentProgress> progress = teacherService.getLessonProgress(lessonId);
        return ResponseEntity.ok(progress);
    }
    
    @GetMapping("/classes/{classId}/stats/average")
    public ResponseEntity<Double> getClassAverageScore(@PathVariable Long classId) {
        Double average = teacherService.getClassAverageScore(classId);
        return ResponseEntity.ok(average);
    }
} 