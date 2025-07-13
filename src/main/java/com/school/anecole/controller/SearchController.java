package com.school.anecole.controller;

import com.school.anecole.model.Lesson;
import com.school.anecole.model.Word;
import com.school.anecole.service.LessonService;
import com.school.anecole.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {
    
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    @Autowired
    private LessonService lessonService;
    
    @Autowired
    private WordService wordService;
    
    /**
     * Поиск по урокам
     */
    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> searchLessons(@RequestParam String query) {
        try {
            List<Lesson> lessons = lessonService.searchLessons(query);
            logger.info("Search lessons with query '{}': found {} results", query, lessons.size());
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            logger.error("Error searching lessons with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Поиск по словам
     */
    @GetMapping("/words")
    public ResponseEntity<List<Word>> searchWords(@RequestParam String query) {
        try {
            List<Word> words = wordService.searchWords(query);
            logger.info("Search words with query '{}': found {} results", query, words.size());
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            logger.error("Error searching words with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Общий поиск по урокам и словам
     */
    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> globalSearch(@RequestParam String query) {
        try {
            Map<String, Object> results = new HashMap<>();
            
            List<Lesson> lessons = lessonService.searchLessons(query);
            List<Word> words = wordService.searchWords(query);
            
            results.put("lessons", lessons);
            results.put("words", words);
            results.put("totalResults", lessons.size() + words.size());
            
            logger.info("Global search with query '{}': found {} lessons, {} words", 
                    query, lessons.size(), words.size());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error global search with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Поиск уроков по классу
     */
    @GetMapping("/lessons/class/{classId}")
    public ResponseEntity<List<Lesson>> searchLessonsByClass(
            @PathVariable Long classId, 
            @RequestParam String query) {
        try {
            List<Lesson> lessons = lessonService.searchLessonsByClass(classId, query);
            logger.info("Search lessons in class {} with query '{}': found {} results", 
                    classId, query, lessons.size());
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            logger.error("Error searching lessons in class {} with query '{}': {}", 
                    classId, query, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Поиск слов по уроку
     */
    @GetMapping("/words/lesson/{lessonId}")
    public ResponseEntity<List<Word>> searchWordsByLesson(
            @PathVariable Long lessonId, 
            @RequestParam String query) {
        try {
            List<Word> words = wordService.searchWordsByLesson(lessonId, query);
            logger.info("Search words in lesson {} with query '{}': found {} results", 
                    lessonId, query, words.size());
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            logger.error("Error searching words in lesson {} with query '{}': {}", 
                    lessonId, query, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 