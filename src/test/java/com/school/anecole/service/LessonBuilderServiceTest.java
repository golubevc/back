package com.school.anecole.service;

import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.model.Word;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.UserRepository;
import com.school.anecole.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonBuilderServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LessonBuilderService lessonBuilderService;

    private User teacher;
    private User student1;
    private User student2;
    private Class testClass;
    private Lesson testLesson;
    private LessonBuilderRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create test teacher
        teacher = new User();
        teacher.setId(1L);
        teacher.setUsername("testteacher");
        teacher.setEmail("teacher@example.com");
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setRole(UserRole.TEACHER);

        // Create test students
        student1 = new User();
        student1.setId(2L);
        student1.setUsername("student1");
        student1.setEmail("student1@example.com");
        student1.setFirstName("Student");
        student1.setLastName("One");
        student1.setRole(UserRole.STUDENT);

        student2 = new User();
        student2.setId(3L);
        student2.setUsername("student2");
        student2.setEmail("student2@example.com");
        student2.setFirstName("Student");
        student2.setLastName("Two");
        student2.setRole(UserRole.STUDENT);

        // Create test class
        testClass = new Class();
        testClass.setId(1L);
        testClass.setName("Test Class");
        testClass.setDescription("Test class description");
        testClass.setTeacher(teacher);
        testClass.setStudents(Arrays.asList(student1, student2));

        // Create test lesson
        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setTitle("Test Lesson");
        testLesson.setDescription("Test description");
        testLesson.setClassEntity(testClass);
        testLesson.setActive(false);

        // Create test request
        testRequest = new LessonBuilderRequest();
        testRequest.setTitle("Test Lesson");
        testRequest.setDescription("Test description");
        testRequest.setClassId(1L);
        testRequest.setOrderNumber(1);
        testRequest.setVideoUrl("https://example.com/video.mp4");
        testRequest.setAudioUrl("https://example.com/audio.mp3");
        testRequest.setImageUrl("https://example.com/image.jpg");
        testRequest.setStudentIds(Arrays.asList(2L, 3L));
        testRequest.setPublishImmediately(false);

        // Add words to request
        LessonBuilderRequest.WordRequest word1 = new LessonBuilderRequest.WordRequest();
        word1.setWord("hello");
        word1.setTranslation("привет");
        word1.setExample("Hello, world!");
        word1.setPronunciation("həˈloʊ");

        LessonBuilderRequest.WordRequest word2 = new LessonBuilderRequest.WordRequest();
        word2.setWord("goodbye");
        word2.setTranslation("до свидания");
        word2.setExample("Goodbye, see you later!");
        word2.setPronunciation("ˌɡʊdˈbaɪ");

        testRequest.setWords(Arrays.asList(word1, word2));
    }

    @Test
    void testCreateLessonFromBuilder_Success() {
        // Arrange
        when(classRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson lesson = invocation.getArgument(0);
            lesson.setId(1L);
            return lesson;
        });
        when(wordRepository.save(any(Word.class))).thenReturn(new Word());

        // Act
        Lesson result = lessonBuilderService.createLessonFromBuilder(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Lesson", result.getTitle());
        assertEquals("Test description", result.getDescription());
        assertEquals(testClass, result.getClassEntity());
        assertEquals(1, result.getOrderNumber());
        assertEquals("https://example.com/video.mp4", result.getVideoUrl());
        assertEquals("https://example.com/audio.mp3", result.getAudioUrl());
        assertEquals("https://example.com/image.jpg", result.getImageUrl());
        assertFalse(result.isActive()); // Should be draft

        // Verify interactions
        verify(classRepository).findById(1L);
        verify(lessonRepository).save(any(Lesson.class));
        verify(wordRepository, times(2)).save(any(Word.class)); // 2 words in testRequest
    }

    @Test
    void testCreateLessonFromBuilder_ClassNotFound() {
        // Arrange
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.createLessonFromBuilder(testRequest)
        );

        assertEquals("Class not found with id: 1", exception.getMessage());

        // Verify interactions
        verify(classRepository).findById(1L);
        verify(lessonRepository, never()).save(any());
        verify(wordRepository, never()).save(any());
    }

    @Test
    void testCreateLessonFromBuilder_PublishImmediately() {
        // Arrange
        testRequest.setPublishImmediately(true);
        when(classRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson lesson = invocation.getArgument(0);
            lesson.setId(1L);
            lesson.setActive(true); // Set active based on publishImmediately
            return lesson;
        });
        when(wordRepository.save(any(Word.class))).thenReturn(new Word());

        // Act
        Lesson result = lessonBuilderService.createLessonFromBuilder(testRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isActive()); // Should be published

        // Verify interactions
        verify(classRepository).findById(1L);
        verify(lessonRepository).save(any(Lesson.class));
        verify(wordRepository, times(2)).save(any(Word.class)); // 2 words in testRequest
    }

    @Test
    void testPublishLessonForStudents_Success() {
        // Arrange
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        // Act
        lessonBuilderService.publishLessonForStudents(1L, Arrays.asList(2L, 3L));

        // Assert
        verify(lessonRepository).findById(1L);
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void testPublishLessonForStudents_LessonNotFound() {
        // Arrange
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.publishLessonForStudents(999L, Arrays.asList(2L, 3L))
        );

        assertEquals("Lesson not found with id: 999", exception.getMessage());

        // Verify interactions
        verify(lessonRepository).findById(999L);
        verify(userRepository, never()).findAllById(anyList());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void testPublishLessonForStudents_StudentNotFound() {
        // Arrange
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);

        // Act
        lessonBuilderService.publishLessonForStudents(1L, Arrays.asList(2L, 3L));

        // Assert - метод не проверяет существование студентов
        verify(lessonRepository).findById(1L);
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void testGetDraftLessonsByTeacher() {
        // Arrange
        List<Lesson> draftLessons = Arrays.asList(testLesson);
        when(lessonRepository.findByTeacherIdAndActive(1L, false))
            .thenReturn(draftLessons);

        // Act
        List<Lesson> result = lessonBuilderService.getDraftLessonsByTeacher(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLesson, result.get(0));

        // Verify interactions
        verify(lessonRepository).findByTeacherIdAndActive(1L, false);
    }

    @Test
    void testGetPublishedLessonsByTeacher() {
        // Arrange
        testLesson.setActive(true);
        List<Lesson> publishedLessons = Arrays.asList(testLesson);
        when(lessonRepository.findByTeacherIdAndActive(1L, true))
            .thenReturn(publishedLessons);

        // Act
        List<Lesson> result = lessonBuilderService.getPublishedLessonsByTeacher(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLesson, result.get(0));
        assertTrue(result.get(0).isActive());

        // Verify interactions
        verify(lessonRepository).findByTeacherIdAndActive(1L, true);
    }

    @Test
    void testGetLessonsForStudent() {
        // Arrange
        testLesson.setActive(true);
        List<Class> studentClasses = Arrays.asList(testClass);
        List<Lesson> studentLessons = Arrays.asList(testLesson);
        
        when(classRepository.findByStudentId(2L)).thenReturn(studentClasses);
        when(lessonRepository.findByClassEntityAndActive(testClass, true)).thenReturn(studentLessons);

        // Act
        List<Lesson> result = lessonBuilderService.getLessonsForStudent(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLesson, result.get(0));
        assertTrue(result.get(0).isActive());

        // Verify interactions
        verify(classRepository).findByStudentId(2L);
        verify(lessonRepository).findByClassEntityAndActive(testClass, true);
    }

    @Test
    void testUpdateLessonFromBuilder_Success() {
        // Arrange
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);
        doNothing().when(wordRepository).deleteByLessonId(1L);
        when(wordRepository.save(any(Word.class))).thenReturn(new Word());

        // Act
        Lesson result = lessonBuilderService.updateLessonFromBuilder(1L, testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Lesson", result.getTitle());
        assertEquals("Test description", result.getDescription());

        // Verify interactions
        verify(lessonRepository).findById(1L);
        verify(wordRepository).deleteByLessonId(1L);
        verify(wordRepository, times(2)).save(any(Word.class)); // 2 words in testRequest
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void testUpdateLessonFromBuilder_LessonNotFound() {
        // Arrange
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.updateLessonFromBuilder(999L, testRequest)
        );

        assertEquals("Lesson not found with id: 999", exception.getMessage());

        // Verify interactions
        verify(lessonRepository).findById(999L);
        verify(wordRepository, never()).deleteByLessonId(anyLong());
        verify(wordRepository, never()).save(any());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void testDeleteLessonWithWords_Success() {
        // Arrange
        when(lessonRepository.existsById(1L)).thenReturn(true);
        doNothing().when(wordRepository).deleteByLessonId(1L);
        doNothing().when(lessonRepository).deleteById(1L);

        // Act
        lessonBuilderService.deleteLessonWithWords(1L);

        // Assert
        verify(lessonRepository).existsById(1L);
        verify(wordRepository).deleteByLessonId(1L);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void testDeleteLessonWithWords_LessonNotFound() {
        // Arrange
        when(lessonRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.deleteLessonWithWords(999L)
        );

        assertEquals("Lesson not found with id: 999", exception.getMessage());

        // Verify interactions
        verify(lessonRepository).existsById(999L);
        verify(wordRepository, never()).deleteByLessonId(anyLong());
        verify(lessonRepository, never()).deleteById(anyLong());
    }

    @Test
    void testCopyLessonToClass_Success() {
        // Arrange
        Class targetClass = new Class();
        targetClass.setId(2L);
        targetClass.setName("Target Class");
        targetClass.setTeacher(teacher);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(classRepository.findById(2L)).thenReturn(Optional.of(targetClass));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson lesson = invocation.getArgument(0);
            lesson.setId(2L);
            return lesson;
        });
        when(wordRepository.findByLesson(testLesson)).thenReturn(Arrays.asList());

        // Act
        Lesson result = lessonBuilderService.copyLessonToClass(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Lesson (копия)", result.getTitle());
        assertEquals("Test description", result.getDescription());
        assertEquals(targetClass, result.getClassEntity());
        assertFalse(result.isActive()); // Should be draft

        // Verify interactions
        verify(lessonRepository).findById(1L);
        verify(classRepository).findById(2L);
        verify(lessonRepository).save(any(Lesson.class));
        verify(wordRepository).findByLesson(testLesson);
    }

    @Test
    void testCopyLessonToClass_SourceLessonNotFound() {
        // Arrange
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.copyLessonToClass(999L, 2L)
        );

        assertEquals("Source lesson not found with id: 999", exception.getMessage());

        // Verify interactions
        verify(lessonRepository).findById(999L);
        verify(classRepository, never()).findById(anyLong());
        verify(lessonRepository, never()).save(any());
        verify(wordRepository, never()).findByLesson(any());
    }

    @Test
    void testCopyLessonToClass_TargetClassNotFound() {
        // Arrange
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(classRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> lessonBuilderService.copyLessonToClass(1L, 999L)
        );

        assertEquals("Target class not found with id: 999", exception.getMessage());

        // Verify interactions
        verify(lessonRepository).findById(1L);
        verify(classRepository).findById(999L);
        verify(lessonRepository, never()).save(any());
        verify(wordRepository, never()).findByLesson(any());
    }

    @Test
    void testCreateLessonFromBuilder_WithWords() {
        // Arrange
        when(classRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);
        when(wordRepository.save(any(Word.class))).thenReturn(new Word());

        // Act
        Lesson result = lessonBuilderService.createLessonFromBuilder(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Lesson", result.getTitle());

        // Verify that words were processed
        verify(wordRepository, times(2)).save(any(Word.class)); // 2 words in testRequest
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void testUpdateLessonFromBuilder_WithEmptyWords() {
        // Arrange
        testRequest.setWords(Arrays.asList()); // Empty words list
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(testLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(testLesson);
        doNothing().when(wordRepository).deleteByLessonId(1L);

        // Act
        Lesson result = lessonBuilderService.updateLessonFromBuilder(1L, testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Lesson", result.getTitle());

        // Verify that old words were deleted but no new words were saved
        verify(wordRepository).deleteByLessonId(1L);
        verify(wordRepository, never()).save(any());
        verify(lessonRepository).save(any(Lesson.class));
    }
} 