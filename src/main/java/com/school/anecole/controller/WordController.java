package com.school.anecole.controller;

import com.school.anecole.model.Word;
import com.school.anecole.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@CrossOrigin(origins = "*")
public class WordController {
    
    private static final Logger logger = LoggerFactory.getLogger(WordController.class);
    
    @Autowired
    private WordService wordService;
    
    @GetMapping
    public ResponseEntity<List<Word>> getAllWords() {
        try {
            List<Word> words = wordService.getAllWords();
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            logger.error("Error getting all words: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable Long id) {
        try {
            Word word = wordService.getWordById(id);
            return ResponseEntity.ok(word);
        } catch (Exception e) {
            logger.error("Error getting word by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Word>> getWordsByLesson(@PathVariable Long lessonId) {
        try {
            List<Word> words = wordService.getWordsByLesson(lessonId);
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            logger.error("Error getting words for lesson {}: {}", lessonId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Word>> searchWords(@RequestParam String term) {
        try {
            List<Word> words = wordService.searchWords(term);
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            logger.error("Error searching words with term '{}': {}", term, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Word> createWord(@RequestBody Word word) {
        try {
            Word createdWord = wordService.createWord(word);
            logger.info("Word created with id: {}", createdWord.getId());
            return ResponseEntity.ok(createdWord);
        } catch (Exception e) {
            logger.error("Error creating word: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Word> updateWord(@PathVariable Long id, @RequestBody Word word) {
        try {
            word.setId(id);
            Word updatedWord = wordService.updateWord(word);
            logger.info("Word updated with id: {}", id);
            return ResponseEntity.ok(updatedWord);
        } catch (Exception e) {
            logger.error("Error updating word {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        try {
            wordService.deleteWord(id);
            logger.info("Word deleted with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting word {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Word> addWordToLesson(@PathVariable Long lessonId, @RequestBody Word word) {
        try {
            Word createdWord = wordService.addWordToLesson(lessonId, word);
            logger.info("Word added to lesson {} with id: {}", lessonId, createdWord.getId());
            return ResponseEntity.ok(createdWord);
        } catch (Exception e) {
            logger.error("Error adding word to lesson {}: {}", lessonId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
} 