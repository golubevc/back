package com.school.anecole.service;

import com.school.anecole.model.Lesson;
import com.school.anecole.model.Word;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WordService {
    
    @Autowired
    private WordRepository wordRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }
    
    public Word getWordById(Long id) {
        return wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found with id: " + id));
    }
    
    public List<Word> getWordsByLesson(Long lessonId) {
        return wordRepository.findByLessonId(lessonId);
    }
    
    public List<Word> searchWords(String term) {
        return wordRepository.findByWordContainingIgnoreCaseOrTranslationContainingIgnoreCase(term, term);
    }
    
    public Word createWord(Word word) {
        return wordRepository.save(word);
    }
    
    public Word updateWord(Word word) {
        if (!wordRepository.existsById(word.getId())) {
            throw new RuntimeException("Word not found with id: " + word.getId());
        }
        return wordRepository.save(word);
    }
    
    public void deleteWord(Long id) {
        if (!wordRepository.existsById(id)) {
            throw new RuntimeException("Word not found with id: " + id);
        }
        wordRepository.deleteById(id);
    }
    
    public Word addWordToLesson(Long lessonId, Word word) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
        
        word.setLesson(lesson);
        return wordRepository.save(word);
    }
} 