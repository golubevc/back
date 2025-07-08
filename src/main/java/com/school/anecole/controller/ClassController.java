package com.school.anecole.controller;

import com.school.anecole.model.Class;
import com.school.anecole.model.User;
import com.school.anecole.service.ClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
public class ClassController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClassController.class);
    
    @Autowired
    private ClassService classService;
    
    @GetMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Class>> getAllClasses() {
        try {
            List<Class> classes = classService.getAllClasses();
            return ResponseEntity.ok(classes);
        } catch (Exception e) {
            logger.error("Error getting all classes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable Long id) {
        try {
            Class classEntity = classService.getClassById(id);
            return ResponseEntity.ok(classEntity);
        } catch (Exception e) {
            logger.error("Error getting class by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Class>> getClassesByTeacher(@PathVariable Long teacherId) {
        try {
            List<Class> classes = classService.getClassesByTeacher(teacherId);
            return ResponseEntity.ok(classes);
        } catch (Exception e) {
            logger.error("Error getting classes for teacher {}: {}", teacherId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Class>> getClassesByStudent(@PathVariable Long studentId) {
        try {
            List<Class> classes = classService.getClassesByStudent(studentId);
            return ResponseEntity.ok(classes);
        } catch (Exception e) {
            logger.error("Error getting classes for student {}: {}", studentId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Class> createClass(@RequestBody Class classEntity) {
        try {
            Class createdClass = classService.createClass(classEntity);
            logger.info("Class created with id: {}", createdClass.getId());
            return ResponseEntity.ok(createdClass);
        } catch (Exception e) {
            logger.error("Error creating class: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Class> updateClass(@PathVariable Long id, @RequestBody Class classEntity) {
        try {
            classEntity.setId(id);
            Class updatedClass = classService.updateClass(classEntity);
            logger.info("Class updated with id: {}", id);
            return ResponseEntity.ok(updatedClass);
        } catch (Exception e) {
            logger.error("Error updating class {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            logger.info("Class deleted with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting class {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{classId}/students/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Class> addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            Class updatedClass = classService.addStudentToClass(classId, studentId);
            logger.info("Student {} added to class {}", studentId, classId);
            return ResponseEntity.ok(updatedClass);
        } catch (Exception e) {
            logger.error("Error adding student {} to class {}: {}", studentId, classId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{classId}/students/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Class> removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            Class updatedClass = classService.removeStudentFromClass(classId, studentId);
            logger.info("Student {} removed from class {}", studentId, classId);
            return ResponseEntity.ok(updatedClass);
        } catch (Exception e) {
            logger.error("Error removing student {} from class {}: {}", studentId, classId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{classId}/students")
    public ResponseEntity<List<User>> getStudentsInClass(@PathVariable Long classId) {
        try {
            List<User> students = classService.getStudentsInClass(classId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            logger.error("Error getting students for class {}: {}", classId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 