package com.school.anecole.controller;

import com.school.anecole.service.StudentService;
import com.school.anecole.service.LessonService;
import com.school.anecole.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {
    
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private LessonService lessonService;
    
    @Autowired
    private WordService wordService;
    
    /**
     * Получить общую статистику для дашборда
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Общая статистика
            stats.put("totalLessons", lessonService.getAllLessons().size());
            stats.put("totalWords", wordService.getAllWords().size());
            stats.put("activeLessons", lessonService.getAllLessons().stream()
                    .filter(lesson -> lesson.isActive()).count());
            
            logger.info("Dashboard stats retrieved successfully");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting dashboard stats: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Получить статистику студента
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.id or hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getStudentStats(@PathVariable Long studentId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Прогресс студента
            var progress = studentService.getStudentProgress(studentId);
            stats.put("progress", progress);
            
            // Количество изученных слов
            long completedWords = progress.stream()
                    .filter(p -> p.isCompleted())
                    .count();
            stats.put("completedWords", completedWords);
            
            // Средний балл
            double averageScore = progress.stream()
                    .mapToDouble(p -> p.getScore())
                    .average()
                    .orElse(0.0);
            stats.put("averageScore", averageScore);
            
            // Количество уроков
            var lessons = studentService.getStudentLessons(studentId);
            stats.put("totalLessons", lessons.size());
            
            logger.info("Student stats retrieved for student: {}", studentId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting student stats for student {}: {}", studentId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Получить статистику учителя
     */
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('TEACHER') and #teacherId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTeacherStats(@PathVariable Long teacherId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Классы учителя
            var classes = studentService.getStudentClasses(teacherId); // Используем тот же сервис
            stats.put("totalClasses", classes.size());
            
            // Уроки учителя
            var lessons = lessonService.getAllLessons(); // TODO: добавить фильтр по учителю
            stats.put("totalLessons", lessons.size());
            
            // Активные уроки
            long activeLessons = lessons.stream()
                    .filter(lesson -> lesson.isActive())
                    .count();
            stats.put("activeLessons", activeLessons);
            
            logger.info("Teacher stats retrieved for teacher: {}", teacherId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting teacher stats for teacher {}: {}", teacherId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Получить статистику по классу
     */
    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getClassStats(@PathVariable Long classId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Уроки класса
            var lessons = lessonService.getLessonsByClass(classId);
            stats.put("totalLessons", lessons.size());
            
            // Активные уроки
            long activeLessons = lessons.stream()
                    .filter(lesson -> lesson.isActive())
                    .count();
            stats.put("activeLessons", activeLessons);
            
            // Общее количество слов в классе
            long totalWords = lessons.stream()
                    .mapToLong(lesson -> lesson.getWords().size())
                    .sum();
            stats.put("totalWords", totalWords);
            
            logger.info("Class stats retrieved for class: {}", classId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting class stats for class {}: {}", classId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Получить статистику по уроку
     */
    @GetMapping("/lesson/{lessonId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getLessonStats(@PathVariable Long lessonId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Слова урока
            var words = wordService.getWordsByLesson(lessonId);
            stats.put("totalWords", words.size());
            
            // Активные слова
            long activeWords = words.stream()
                    .filter(word -> word.isActive())
                    .count();
            stats.put("activeWords", activeWords);
            
            logger.info("Lesson stats retrieved for lesson: {}", lessonId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting lesson stats for lesson {}: {}", lessonId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 