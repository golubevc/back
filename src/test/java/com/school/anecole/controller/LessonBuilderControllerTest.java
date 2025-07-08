package com.school.anecole.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.service.LessonBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LessonBuilderControllerTest {

    @Mock
    private LessonBuilderService lessonBuilderService;

    @InjectMocks
    private LessonBuilderController lessonBuilderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User teacher;
    private User student1;
    private User student2;
    private Class testClass;
    private Lesson testLesson;
    private LessonBuilderRequest testRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonBuilderController).build();
        objectMapper = new ObjectMapper();

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
        testLesson.setOrderNumber(1);
        testLesson.setVideoUrl("https://example.com/video.mp4");
        testLesson.setAudioUrl("https://example.com/audio.mp3");
        testLesson.setImageUrl("https://example.com/image.jpg");
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
    void testCreateLesson_Success() throws Exception {
        // Arrange
        when(lessonBuilderService.createLessonFromBuilder(any(LessonBuilderRequest.class)))
            .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Lesson"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.orderNumber").value(1))
                .andExpect(jsonPath("$.videoUrl").value("https://example.com/video.mp4"))
                .andExpect(jsonPath("$.audioUrl").value("https://example.com/audio.mp3"))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$.active").value(false));

        // Verify service method was called
        verify(lessonBuilderService).createLessonFromBuilder(any(LessonBuilderRequest.class));
    }

    @Test
    void testCreateLesson_ValidationError() throws Exception {
        // Arrange
        LessonBuilderRequest invalidRequest = new LessonBuilderRequest();
        invalidRequest.setTitle(""); // Empty title
        invalidRequest.setDescription(""); // Empty description
        // Missing classId

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify service method was not called
        verify(lessonBuilderService, never()).createLessonFromBuilder(any());
    }

    @Test
    void testCreateLesson_ServiceException() throws Exception {
        // Arrange
        when(lessonBuilderService.createLessonFromBuilder(any(LessonBuilderRequest.class)))
            .thenThrow(new IllegalArgumentException("Класс с ID 1 не найден"));

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest());

        // Verify service method was called
        verify(lessonBuilderService).createLessonFromBuilder(any(LessonBuilderRequest.class));
    }

    @Test
    void testUpdateLesson_Success() throws Exception {
        // Arrange
        Lesson updatedLesson = new Lesson();
        updatedLesson.setId(1L);
        updatedLesson.setTitle("Updated Lesson");
        updatedLesson.setDescription("Updated description");
        updatedLesson.setClassEntity(testClass);
        updatedLesson.setOrderNumber(2);
        updatedLesson.setActive(false);

        when(lessonBuilderService.updateLessonFromBuilder(eq(1L), any(LessonBuilderRequest.class)))
            .thenReturn(updatedLesson);

        // Act & Assert
        mockMvc.perform(put("/api/lesson-builder/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Lesson"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.orderNumber").value(2));

        // Verify service method was called
        verify(lessonBuilderService).updateLessonFromBuilder(eq(1L), any(LessonBuilderRequest.class));
    }

    @Test
    void testUpdateLesson_ServiceException() throws Exception {
        // Arrange
        when(lessonBuilderService.updateLessonFromBuilder(eq(999L), any(LessonBuilderRequest.class)))
            .thenThrow(new IllegalArgumentException("Урок с ID 999 не найден"));

        // Act & Assert
        mockMvc.perform(put("/api/lesson-builder/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest());

        // Verify service method was called
        verify(lessonBuilderService).updateLessonFromBuilder(eq(999L), any(LessonBuilderRequest.class));
    }

    @Test
    void testPublishLesson_Success() throws Exception {
        // Arrange
        List<Long> studentIds = Arrays.asList(2L, 3L);
        doNothing().when(lessonBuilderService).publishLessonForStudents(eq(1L), eq(studentIds));

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/1/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentIds)))
                .andExpect(status().isOk());

        // Verify service method was called
        verify(lessonBuilderService).publishLessonForStudents(eq(1L), eq(studentIds));
    }

    @Test
    void testPublishLesson_ServiceException() throws Exception {
        // Arrange
        List<Long> studentIds = Arrays.asList(2L, 3L);
        doThrow(new IllegalArgumentException("Урок с ID 999 не найден"))
            .when(lessonBuilderService).publishLessonForStudents(eq(999L), eq(studentIds));

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/999/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentIds)))
                .andExpect(status().isBadRequest());

        // Verify service method was called
        verify(lessonBuilderService).publishLessonForStudents(eq(999L), eq(studentIds));
    }

    @Test
    void testGetDraftLessons_Success() throws Exception {
        // Arrange
        List<Lesson> draftLessons = Arrays.asList(testLesson);
        when(lessonBuilderService.getDraftLessonsByTeacher(1L)).thenReturn(draftLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/teacher/1/drafts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Lesson"))
                .andExpect(jsonPath("$[0].active").value(false));

        // Verify service method was called
        verify(lessonBuilderService).getDraftLessonsByTeacher(1L);
    }

    @Test
    void testGetPublishedLessons_Success() throws Exception {
        // Arrange
        testLesson.setActive(true);
        List<Lesson> publishedLessons = Arrays.asList(testLesson);
        when(lessonBuilderService.getPublishedLessonsByTeacher(1L)).thenReturn(publishedLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/teacher/1/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Lesson"))
                .andExpect(jsonPath("$[0].active").value(true));

        // Verify service method was called
        verify(lessonBuilderService).getPublishedLessonsByTeacher(1L);
    }

    @Test
    void testGetStudentLessons_Success() throws Exception {
        // Arrange
        testLesson.setActive(true);
        List<Lesson> studentLessons = Arrays.asList(testLesson);
        when(lessonBuilderService.getLessonsForStudent(2L)).thenReturn(studentLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/student/2/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Lesson"))
                .andExpect(jsonPath("$[0].active").value(true));

        // Verify service method was called
        verify(lessonBuilderService).getLessonsForStudent(2L);
    }

    @Test
    void testDeleteLesson_Success() throws Exception {
        // Arrange
        doNothing().when(lessonBuilderService).deleteLessonWithWords(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/lesson-builder/1"))
                .andExpect(status().isOk());

        // Verify service method was called
        verify(lessonBuilderService).deleteLessonWithWords(1L);
    }

    @Test
    void testDeleteLesson_ServiceException() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Урок с ID 999 не найден"))
            .when(lessonBuilderService).deleteLessonWithWords(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/lesson-builder/999"))
                .andExpect(status().isBadRequest());

        // Verify service method was called
        verify(lessonBuilderService).deleteLessonWithWords(999L);
    }

    @Test
    void testCopyLesson_Success() throws Exception {
        // Arrange
        Lesson copiedLesson = new Lesson();
        copiedLesson.setId(2L);
        copiedLesson.setTitle("Test Lesson (копия)");
        copiedLesson.setDescription("Test description");
        copiedLesson.setClassEntity(testClass);
        copiedLesson.setActive(false);

        when(lessonBuilderService.copyLessonToClass(eq(1L), eq(2L))).thenReturn(copiedLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/1/copy")
                .param("targetClassId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Test Lesson (копия)"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.active").value(false));

        // Verify service method was called
        verify(lessonBuilderService).copyLessonToClass(eq(1L), eq(2L));
    }

    @Test
    void testCopyLesson_ServiceException() throws Exception {
        // Arrange
        when(lessonBuilderService.copyLessonToClass(eq(999L), eq(2L)))
            .thenThrow(new IllegalArgumentException("Исходный урок с ID 999 не найден"));

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/999/copy")
                .param("targetClassId", "2"))
                .andExpect(status().isBadRequest());

        // Verify service method was called
        verify(lessonBuilderService).copyLessonToClass(eq(999L), eq(2L));
    }

    @Test
    void testPreviewLesson_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/preview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Lesson"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.orderNumber").value(1))
                .andExpect(jsonPath("$.videoUrl").value("https://example.com/video.mp4"))
                .andExpect(jsonPath("$.audioUrl").value("https://example.com/audio.mp3"))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/image.jpg"));

        // Verify no service method was called (preview doesn't use service)
        verify(lessonBuilderService, never()).createLessonFromBuilder(any());
    }

    @Test
    void testPreviewLesson_ValidationError() throws Exception {
        // Arrange
        LessonBuilderRequest invalidRequest = new LessonBuilderRequest();
        invalidRequest.setTitle(""); // Empty title
        invalidRequest.setDescription(""); // Empty description
        // Missing classId

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/preview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify no service method was called
        verify(lessonBuilderService, never()).createLessonFromBuilder(any());
    }

    @Test
    void testCreateLesson_WithWords() throws Exception {
        // Arrange
        when(lessonBuilderService.createLessonFromBuilder(any(LessonBuilderRequest.class)))
            .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Lesson"));

        // Verify service method was called with words
        verify(lessonBuilderService).createLessonFromBuilder(argThat(request -> 
            request.getWords() != null && request.getWords().size() == 2 &&
            request.getWords().get(0).getWord().equals("hello") &&
            request.getWords().get(1).getWord().equals("goodbye")
        ));
    }

    @Test
    void testCreateLesson_WithPublishImmediately() throws Exception {
        // Arrange
        testRequest.setPublishImmediately(true);
        testLesson.setActive(true);
        when(lessonBuilderService.createLessonFromBuilder(any(LessonBuilderRequest.class)))
            .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        // Verify service method was called with publishImmediately = true
        verify(lessonBuilderService).createLessonFromBuilder(argThat(request -> 
            request.getPublishImmediately()
        ));
    }

    @Test
    void testUpdateLesson_WithEmptyWords() throws Exception {
        // Arrange
        testRequest.setWords(Arrays.asList()); // Empty words list
        when(lessonBuilderService.updateLessonFromBuilder(eq(1L), any(LessonBuilderRequest.class)))
            .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(put("/api/lesson-builder/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk());

        // Verify service method was called with empty words list
        verify(lessonBuilderService).updateLessonFromBuilder(eq(1L), argThat(request -> 
            request.getWords() != null && request.getWords().isEmpty()
        ));
    }
} 