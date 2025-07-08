package com.school.anecole.controller;

import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Lesson;
import com.school.anecole.service.LessonBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lesson-builder")
@CrossOrigin(origins = "*")
public class LessonBuilderController {
    
    @Autowired
    private LessonBuilderService lessonBuilderService;
    
    /**
     * Создает новый урок через конструктор
     */
    @PostMapping("/create")
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody LessonBuilderRequest request) {
        try {
            Lesson createdLesson = lessonBuilderService.createLessonFromBuilder(request);
            return ResponseEntity.ok(createdLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Обновляет существующий урок через конструктор
     */
    @PutMapping("/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonBuilderRequest request) {
        try {
            Lesson updatedLesson = lessonBuilderService.updateLessonFromBuilder(lessonId, request);
            return ResponseEntity.ok(updatedLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Публикует урок для конкретных учеников
     */
    @PostMapping("/{lessonId}/publish")
    public ResponseEntity<Void> publishLesson(
            @PathVariable Long lessonId,
            @RequestBody List<Long> studentIds) {
        try {
            lessonBuilderService.publishLessonForStudents(lessonId, studentIds);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Получает черновики уроков для учителя
     */
    @GetMapping("/teacher/{teacherId}/drafts")
    public ResponseEntity<List<Lesson>> getDraftLessons(@PathVariable Long teacherId) {
        List<Lesson> drafts = lessonBuilderService.getDraftLessonsByTeacher(teacherId);
        return ResponseEntity.ok(drafts);
    }
    
    /**
     * Получает опубликованные уроки для учителя
     */
    @GetMapping("/teacher/{teacherId}/published")
    public ResponseEntity<List<Lesson>> getPublishedLessons(@PathVariable Long teacherId) {
        List<Lesson> published = lessonBuilderService.getPublishedLessonsByTeacher(teacherId);
        return ResponseEntity.ok(published);
    }
    
    /**
     * Получает уроки для конкретного ученика
     */
    @GetMapping("/student/{studentId}/lessons")
    public ResponseEntity<List<Lesson>> getStudentLessons(@PathVariable Long studentId) {
        List<Lesson> lessons = lessonBuilderService.getLessonsForStudent(studentId);
        return ResponseEntity.ok(lessons);
    }
    
    /**
     * Удаляет урок и все связанные с ним слова
     */
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        try {
            lessonBuilderService.deleteLessonWithWords(lessonId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Копирует урок в другой класс
     */
    @PostMapping("/{lessonId}/copy")
    public ResponseEntity<Lesson> copyLesson(
            @PathVariable Long lessonId,
            @RequestParam Long targetClassId) {
        try {
            Lesson copiedLesson = lessonBuilderService.copyLessonToClass(lessonId, targetClassId);
            return ResponseEntity.ok(copiedLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Предварительный просмотр урока (без сохранения)
     */
    @PostMapping("/preview")
    public ResponseEntity<Lesson> previewLesson(@Valid @RequestBody LessonBuilderRequest request) {
        // Создаем временный объект для предварительного просмотра
        Lesson previewLesson = new Lesson();
        previewLesson.setTitle(request.getTitle());
        previewLesson.setDescription(request.getDescription());
        previewLesson.setOrderNumber(request.getOrderNumber());
        previewLesson.setVideoUrl(request.getVideoUrl());
        previewLesson.setAudioUrl(request.getAudioUrl());
        previewLesson.setImageUrl(request.getImageUrl());
        
        // Добавляем слова для предварительного просмотра
        if (request.getWords() != null && !request.getWords().isEmpty()) {
            // Здесь можно добавить логику для создания временных слов
        }
        
        return ResponseEntity.ok(previewLesson);
    }
} 