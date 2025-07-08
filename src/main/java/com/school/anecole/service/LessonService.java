package com.school.anecole.service;

import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.Word;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private WordRepository wordRepository;
    
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }
    
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }
    
    public List<Lesson> getLessonsByClass(Long classId) {
        Class classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        return lessonRepository.findByClassEntityOrderByOrderNumber(classEntity);
    }
    
    public List<Lesson> getActiveLessonsByClass(Long classId) {
        return lessonRepository.findActiveByClassIdOrderByOrderNumber(classId);
    }
    
    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    
    public Lesson updateLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setActive(false);
        lessonRepository.save(lesson);
    }
    
    public List<Word> getLessonWords(Long lessonId) {
        return wordRepository.findActiveByLessonId(lessonId);
    }
    
    public Word addWordToLesson(Long lessonId, Word word) {
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        
        word.setLesson(lesson);
        return wordRepository.save(word);
    }
    
    public Word updateWord(Word word) {
        return wordRepository.save(word);
    }
    
    public void deleteWord(Long wordId) {
        Word word = wordRepository.findById(wordId)
            .orElseThrow(() -> new RuntimeException("Word not found"));
        word.setActive(false);
        wordRepository.save(word);
    }
    
    public List<Word> searchWords(String searchTerm) {
        return wordRepository.findByWordOrTranslationContaining(searchTerm);
    }
    
    public List<Word> getWordsByClass(Long classId) {
        return wordRepository.findActiveByClassId(classId);
    }
    
    public Lesson getNextLesson(Long currentLessonId, Long classId) {
        Lesson currentLesson = lessonRepository.findById(currentLessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        
        List<Lesson> classLessons = lessonRepository.findActiveByClassIdOrderByOrderNumber(classId);
        
        for (int i = 0; i < classLessons.size(); i++) {
            if (classLessons.get(i).getId().equals(currentLessonId) && i + 1 < classLessons.size()) {
                return classLessons.get(i + 1);
            }
        }
        
        return null; // No next lesson
    }
    
    public Lesson getPreviousLesson(Long currentLessonId, Long classId) {
        Lesson currentLesson = lessonRepository.findById(currentLessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        
        List<Lesson> classLessons = lessonRepository.findActiveByClassIdOrderByOrderNumber(classId);
        
        for (int i = 0; i < classLessons.size(); i++) {
            if (classLessons.get(i).getId().equals(currentLessonId) && i > 0) {
                return classLessons.get(i - 1);
            }
        }
        
        return null; // No previous lesson
    }
} 