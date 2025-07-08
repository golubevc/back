package com.school.anecole.controller;

import com.school.anecole.model.Lesson;
import com.school.anecole.model.Word;
import com.school.anecole.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {
    
    @Autowired
    private LessonService lessonService;
    
    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Lesson>> getLessonsByClass(@PathVariable Long classId) {
        List<Lesson> lessons = lessonService.getLessonsByClass(classId);
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/class/{classId}/active")
    public ResponseEntity<List<Lesson>> getActiveLessonsByClass(@PathVariable Long classId) {
        List<Lesson> lessons = lessonService.getActiveLessonsByClass(classId);
        return ResponseEntity.ok(lessons);
    }
    
    @PostMapping
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        Lesson createdLesson = lessonService.createLesson(lesson);
        return ResponseEntity.ok(createdLesson);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        lesson.setId(id);
        Lesson updatedLesson = lessonService.updateLesson(lesson);
        return ResponseEntity.ok(updatedLesson);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{lessonId}/words")
    public ResponseEntity<List<Word>> getLessonWords(@PathVariable Long lessonId) {
        List<Word> words = lessonService.getLessonWords(lessonId);
        return ResponseEntity.ok(words);
    }
    
    @PostMapping("/{lessonId}/words")
    public ResponseEntity<Word> addWordToLesson(
            @PathVariable Long lessonId,
            @RequestBody Word word) {
        Word createdWord = lessonService.addWordToLesson(lessonId, word);
        return ResponseEntity.ok(createdWord);
    }
    
    @PutMapping("/words/{wordId}")
    public ResponseEntity<Word> updateWord(@PathVariable Long wordId, @RequestBody Word word) {
        word.setId(wordId);
        Word updatedWord = lessonService.updateWord(word);
        return ResponseEntity.ok(updatedWord);
    }
    
    @DeleteMapping("/words/{wordId}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long wordId) {
        lessonService.deleteWord(wordId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/words/search")
    public ResponseEntity<List<Word>> searchWords(@RequestParam String term) {
        List<Word> words = lessonService.searchWords(term);
        return ResponseEntity.ok(words);
    }
    
    @GetMapping("/words/class/{classId}")
    public ResponseEntity<List<Word>> getWordsByClass(@PathVariable Long classId) {
        List<Word> words = lessonService.getWordsByClass(classId);
        return ResponseEntity.ok(words);
    }
    
    @GetMapping("/{currentLessonId}/next")
    public ResponseEntity<Lesson> getNextLesson(
            @PathVariable Long currentLessonId,
            @RequestParam Long classId) {
        Lesson nextLesson = lessonService.getNextLesson(currentLessonId, classId);
        return nextLesson != null ? ResponseEntity.ok(nextLesson) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{currentLessonId}/previous")
    public ResponseEntity<Lesson> getPreviousLesson(
            @PathVariable Long currentLessonId,
            @RequestParam Long classId) {
        Lesson previousLesson = lessonService.getPreviousLesson(currentLessonId, classId);
        return previousLesson != null ? ResponseEntity.ok(previousLesson) : ResponseEntity.notFound().build();
    }
} 