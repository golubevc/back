package com.school.anecole.service;

import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.User;
import com.school.anecole.model.Word;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.UserRepository;
import com.school.anecole.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonBuilderService {
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private WordRepository wordRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Основные методы для создания и управления уроками
    
    public Lesson createLesson(LessonBuilderRequest request) {
        return createLessonFromBuilder(request);
    }
    
    public Lesson createLessonFromBuilder(LessonBuilderRequest request) {
        // Проверяем существование класса
        Class classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with id: " + request.getClassId()));
        
        // Создаем урок
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setClassEntity(classEntity);
        lesson.setOrderNumber(request.getOrderNumber());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setAudioUrl(request.getAudioUrl());
        lesson.setImageUrl(request.getImageUrl());
        lesson.setActive(request.getPublishImmediately());
        
        Lesson savedLesson = lessonRepository.save(lesson);
        
        // Добавляем слова
        if (request.getWords() != null) {
            for (LessonBuilderRequest.WordRequest wordRequest : request.getWords()) {
                Word word = new Word();
                word.setWord(wordRequest.getWord());
                word.setTranslation(wordRequest.getTranslation());
                word.setExample(wordRequest.getExample());
                word.setPronunciation(wordRequest.getPronunciation());
                word.setAudioUrl(wordRequest.getAudioUrl());
                word.setImageUrl(wordRequest.getImageUrl());
                word.setLesson(savedLesson);
                word.setActive(true);
                
                wordRepository.save(word);
            }
        }
        
        return savedLesson;
    }
    
    public Lesson updateLesson(long lessonId, LessonBuilderRequest request) {
        return updateLessonFromBuilder(lessonId, request);
    }
    
    public Lesson updateLessonFromBuilder(Long lessonId, LessonBuilderRequest request) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        
        // Обновляем данные урока
        existingLesson.setTitle(request.getTitle());
        existingLesson.setDescription(request.getDescription());
        existingLesson.setOrderNumber(request.getOrderNumber());
        existingLesson.setVideoUrl(request.getVideoUrl());
        existingLesson.setAudioUrl(request.getAudioUrl());
        existingLesson.setImageUrl(request.getImageUrl());
        
        Lesson updatedLesson = lessonRepository.save(existingLesson);
        
        // Обновляем слова
        if (request.getWords() != null) {
            // Удаляем старые слова
            wordRepository.deleteByLessonId(lessonId);
            
            // Добавляем новые слова
            for (LessonBuilderRequest.WordRequest wordRequest : request.getWords()) {
                Word word = new Word();
                word.setWord(wordRequest.getWord());
                word.setTranslation(wordRequest.getTranslation());
                word.setExample(wordRequest.getExample());
                word.setPronunciation(wordRequest.getPronunciation());
                word.setAudioUrl(wordRequest.getAudioUrl());
                word.setImageUrl(wordRequest.getImageUrl());
                word.setLesson(updatedLesson);
                word.setActive(true);
                
                wordRepository.save(word);
            }
        }
        
        return updatedLesson;
    }
    
    public void publishLesson(long lessonId) {
        publishLessonForStudents(lessonId, null);
    }
    
    public void publishLessonForStudents(Long lessonId, List<Long> studentIds) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        
        lesson.setActive(true);
        lessonRepository.save(lesson);
        
        // Здесь можно добавить логику для назначения урока конкретным ученикам
        // Например, создание записей в таблице StudentProgress
    }
    
    public List<Lesson> getDraftLessons(Long teacherId) {
        return getDraftLessonsByTeacher(teacherId);
    }
    
    public List<Lesson> getDraftLessonsByTeacher(Long teacherId) {
        return lessonRepository.findByTeacherIdAndActive(teacherId, false);
    }
    
    public List<Lesson> getPublishedLessons(Long teacherId) {
        return getPublishedLessonsByTeacher(teacherId);
    }
    
    public List<Lesson> getPublishedLessonsByTeacher(Long teacherId) {
        return lessonRepository.findByTeacherIdAndActive(teacherId, true);
    }
    
    public List<Lesson> getStudentLessons(long studentId) {
        return getLessonsForStudent(studentId);
    }
    
    public List<Lesson> getLessonsForStudent(Long studentId) {
        // Получаем классы ученика и их активные уроки
        List<Class> studentClasses = classRepository.findByStudentId(studentId);
        return studentClasses.stream()
                .flatMap(classEntity -> lessonRepository.findByClassEntityAndActive(classEntity, true).stream())
                .collect(Collectors.toList());
    }
    
    public Lesson previewLesson(LessonBuilderRequest request) {
        // Создаем временный урок для предварительного просмотра
        Class classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with id: " + request.getClassId()));
        
        Lesson previewLesson = new Lesson();
        previewLesson.setTitle(request.getTitle());
        previewLesson.setDescription(request.getDescription());
        previewLesson.setClassEntity(classEntity);
        previewLesson.setOrderNumber(request.getOrderNumber());
        previewLesson.setVideoUrl(request.getVideoUrl());
        previewLesson.setAudioUrl(request.getAudioUrl());
        previewLesson.setImageUrl(request.getImageUrl());
        previewLesson.setActive(false); // Предварительный просмотр всегда неактивен
        
        return previewLesson;
    }
    
    public Lesson copyLesson(long lessonId) {
        // Копируем урок в тот же класс
        Lesson sourceLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Source lesson not found with id: " + lessonId));
        
        return copyLessonToClass(lessonId, sourceLesson.getClassEntity().getId());
    }
    
    public Lesson copyLessonToClass(Long lessonId, Long targetClassId) {
        Lesson sourceLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Source lesson not found with id: " + lessonId));
        
        Class targetClass = classRepository.findById(targetClassId)
                .orElseThrow(() -> new IllegalArgumentException("Target class not found with id: " + targetClassId));
        
        // Создаем копию урока
        Lesson copiedLesson = new Lesson();
        copiedLesson.setTitle(sourceLesson.getTitle() + " (копия)");
        copiedLesson.setDescription(sourceLesson.getDescription());
        copiedLesson.setClassEntity(targetClass);
        copiedLesson.setOrderNumber(sourceLesson.getOrderNumber());
        copiedLesson.setVideoUrl(sourceLesson.getVideoUrl());
        copiedLesson.setAudioUrl(sourceLesson.getAudioUrl());
        copiedLesson.setImageUrl(sourceLesson.getImageUrl());
        copiedLesson.setActive(false); // Копия создается как черновик
        
        Lesson savedLesson = lessonRepository.save(copiedLesson);
        
        // Копируем слова
        List<Word> sourceWords = wordRepository.findByLesson(sourceLesson);
        for (Word sourceWord : sourceWords) {
            Word copiedWord = new Word();
            copiedWord.setWord(sourceWord.getWord());
            copiedWord.setTranslation(sourceWord.getTranslation());
            copiedWord.setExample(sourceWord.getExample());
            copiedWord.setPronunciation(sourceWord.getPronunciation());
            copiedWord.setAudioUrl(sourceWord.getAudioUrl());
            copiedWord.setImageUrl(sourceWord.getImageUrl());
            copiedWord.setLesson(savedLesson);
            copiedWord.setActive(true);
            
            wordRepository.save(copiedWord);
        }
        
        return savedLesson;
    }
    
    public void deleteLessonWithWords(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new IllegalArgumentException("Lesson not found with id: " + lessonId);
        }
        
        // Удаляем слова урока
        wordRepository.deleteByLessonId(lessonId);
        
        // Удаляем урок
        lessonRepository.deleteById(lessonId);
    }
} 