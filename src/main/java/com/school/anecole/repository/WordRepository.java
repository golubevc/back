package com.school.anecole.repository;

import com.school.anecole.model.Lesson;
import com.school.anecole.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    
    List<Word> findByLesson(Lesson lesson);
    
    List<Word> findByLessonAndActive(Lesson lesson, boolean active);
    
    @Query("SELECT w FROM Word w WHERE w.lesson.id = :lessonId AND w.active = true")
    List<Word> findActiveByLessonId(@Param("lessonId") Long lessonId);
    
    @Query("SELECT w FROM Word w WHERE w.lesson.classEntity.id = :classId AND w.active = true")
    List<Word> findActiveByClassId(@Param("classId") Long classId);
    
    @Query("SELECT w FROM Word w WHERE w.word LIKE %:searchTerm% OR w.translation LIKE %:searchTerm%")
    List<Word> findByWordOrTranslationContaining(@Param("searchTerm") String searchTerm);
    
    void deleteByLessonId(Long lessonId);
    
    // Новые методы для WordService
    @Query("SELECT w FROM Word w WHERE w.lesson.id = :lessonId")
    List<Word> findByLessonId(@Param("lessonId") Long lessonId);
    
    List<Word> findByWordContainingIgnoreCaseOrTranslationContainingIgnoreCase(String word, String translation);
    
    // Метод для поиска слов по уроку
    @Query("SELECT w FROM Word w WHERE w.lesson.id = :lessonId AND (w.word LIKE %:query% OR w.translation LIKE %:query%)")
    List<Word> findByLessonIdAndWordContainingIgnoreCaseOrLessonIdAndTranslationContainingIgnoreCase(
            @Param("lessonId") Long lessonId, @Param("query") String query);
} 