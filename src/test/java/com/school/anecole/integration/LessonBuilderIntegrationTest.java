package com.school.anecole.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.service.LessonBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.school.anecole.controller.LessonBuilderController.class)
@ActiveProfiles("test")
class LessonBuilderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonBuilderService lessonBuilderService;

    private ObjectMapper objectMapper;
    private LessonBuilderRequest testLessonRequest;
    private Lesson testLesson;
    private User testTeacher;
    private Class testClass;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        // Create test data
        createTestData();
    }

    private void createTestData() {
        // Create test teacher
        testTeacher = new User();
        testTeacher.setId(1L);
        testTeacher.setUsername("testteacher");
        testTeacher.setRole(UserRole.TEACHER);

        // Create test class
        testClass = new Class();
        testClass.setId(1L);
        testClass.setName("Test Class");
        testClass.setTeacher(testTeacher);

        // Create test lesson
        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setTitle("Тестовый урок");
        testLesson.setDescription("Описание тестового урока");
        testLesson.setClassEntity(testClass);
        testLesson.setActive(false);

        // Create test lesson request
        testLessonRequest = new LessonBuilderRequest();
        testLessonRequest.setTitle("Тестовый урок");
        testLessonRequest.setDescription("Описание тестового урока");
        testLessonRequest.setClassId(testClass.getId());
        testLessonRequest.setOrderNumber(1);
        testLessonRequest.setVideoUrl("https://example.com/video.mp4");
        testLessonRequest.setAudioUrl("https://example.com/audio.mp3");
        testLessonRequest.setImageUrl("https://example.com/image.jpg");
        testLessonRequest.setPublishImmediately(false);

        // Add words
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

        testLessonRequest.setWords(Arrays.asList(word1, word2));
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testCreateLesson() throws Exception {
        System.out.println("🧪 Тестирование создания урока...");

        when(lessonBuilderService.createLesson(any(LessonBuilderRequest.class)))
                .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLessonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Тестовый урок"))
                .andExpect(jsonPath("$.description").value("Описание тестового урока"))
                .andExpect(jsonPath("$.active").value(false));

        System.out.println("✅ Урок создан успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testGetDraftLessons() throws Exception {
        System.out.println("🧪 Тестирование получения черновиков...");

        List<Lesson> draftLessons = Arrays.asList(testLesson);
        when(lessonBuilderService.getDraftLessons(testTeacher.getId()))
                .thenReturn(draftLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/teacher/" + testTeacher.getId() + "/drafts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Тестовый урок"))
                .andExpect(jsonPath("$[0].active").value(false));

        System.out.println("✅ Черновики получены успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testUpdateLesson() throws Exception {
        System.out.println("🧪 Тестирование обновления урока...");

        Lesson updatedLesson = new Lesson();
        updatedLesson.setId(1L);
        updatedLesson.setTitle("Обновленный урок");
        updatedLesson.setDescription("Обновленное описание");
        updatedLesson.setClassEntity(testClass);
        updatedLesson.setActive(false);

        when(lessonBuilderService.updateLesson(eq(1L), any(LessonBuilderRequest.class)))
                .thenReturn(updatedLesson);

        // Act & Assert
        mockMvc.perform(put("/api/lesson-builder/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLessonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновленный урок"))
                .andExpect(jsonPath("$.description").value("Обновленное описание"));

        System.out.println("✅ Урок обновлен успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testPublishLesson() throws Exception {
        System.out.println("🧪 Тестирование публикации урока...");

        Lesson publishedLesson = new Lesson();
        publishedLesson.setId(1L);
        publishedLesson.setTitle("Тестовый урок");
        publishedLesson.setActive(true);

        doNothing().when(lessonBuilderService).publishLesson(1L);

        // Act & Assert
        mockMvc.perform(put("/api/lesson-builder/1/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        System.out.println("✅ Урок опубликован успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testGetPublishedLessons() throws Exception {
        System.out.println("🧪 Тестирование получения опубликованных уроков...");

        Lesson publishedLesson = new Lesson();
        publishedLesson.setId(1L);
        publishedLesson.setTitle("Тестовый урок");
        publishedLesson.setActive(true);

        List<Lesson> publishedLessons = Arrays.asList(publishedLesson);
        when(lessonBuilderService.getPublishedLessons(testTeacher.getId()))
                .thenReturn(publishedLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/teacher/" + testTeacher.getId() + "/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Тестовый урок"))
                .andExpect(jsonPath("$[0].active").value(true));

        System.out.println("✅ Опубликованные уроки получены успешно");
    }

    @Test
    @WithMockUser(username = "student1", roles = {"STUDENT"})
    void testGetStudentLessons() throws Exception {
        System.out.println("🧪 Тестирование получения уроков для студента...");

        List<Lesson> studentLessons = Arrays.asList(testLesson);
        when(lessonBuilderService.getStudentLessons(1L))
                .thenReturn(studentLessons);

        // Act & Assert
        mockMvc.perform(get("/api/lesson-builder/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Тестовый урок"));

        System.out.println("✅ Уроки для студента получены успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testPreviewLesson() throws Exception {
        System.out.println("🧪 Тестирование предварительного просмотра урока...");

        when(lessonBuilderService.previewLesson(any(LessonBuilderRequest.class)))
                .thenReturn(testLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/preview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLessonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Тестовый урок"))
                .andExpect(jsonPath("$.description").value("Описание тестового урока"));

        System.out.println("✅ Предварительный просмотр работает успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testCopyLesson() throws Exception {
        System.out.println("🧪 Тестирование копирования урока...");

        Lesson copiedLesson = new Lesson();
        copiedLesson.setId(2L);
        copiedLesson.setTitle("Тестовый урок (копия)");
        copiedLesson.setDescription("Описание тестового урока");
        copiedLesson.setClassEntity(testClass);
        copiedLesson.setActive(false);

        when(lessonBuilderService.copyLesson(1L))
                .thenReturn(copiedLesson);

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/1/copy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Тестовый урок (копия)"))
                .andExpect(jsonPath("$.active").value(false));

        System.out.println("✅ Урок скопирован успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testDeleteLesson() throws Exception {
        System.out.println("🧪 Тестирование удаления урока...");

        // Act & Assert
        mockMvc.perform(delete("/api/lesson-builder/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Урок успешно удален"));

        System.out.println("✅ Урок удален успешно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testCreateLessonWithInvalidData() throws Exception {
        System.out.println("🧪 Тестирование создания урока с неверными данными...");

        LessonBuilderRequest invalidRequest = new LessonBuilderRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        System.out.println("✅ Валидация работает корректно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testCreateLessonWithNonExistentClass() throws Exception {
        System.out.println("🧪 Тестирование создания урока с несуществующим классом...");

        LessonBuilderRequest requestWithInvalidClass = new LessonBuilderRequest();
        requestWithInvalidClass.setTitle("Тестовый урок");
        requestWithInvalidClass.setDescription("Описание");
        requestWithInvalidClass.setClassId(999L); // Non-existent class

        when(lessonBuilderService.createLesson(any(LessonBuilderRequest.class)))
                .thenThrow(new RuntimeException("Класс не найден"));

        // Act & Assert
        mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithInvalidClass)))
                .andExpect(status().isInternalServerError());

        System.out.println("✅ Обработка несуществующего класса работает корректно");
    }

    @Test
    @WithMockUser(username = "testteacher", roles = {"TEACHER"})
    void testFullLessonBuilderWorkflow() throws Exception {
        System.out.println("🧪 Тестирование полного workflow создания урока...");

        // Step 1: Create lesson
        when(lessonBuilderService.createLesson(any(LessonBuilderRequest.class)))
                .thenReturn(testLesson);

        String createResponse = mockMvc.perform(post("/api/lesson-builder/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLessonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.active").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("✅ Шаг 1: Урок создан");

        // Step 2: Update lesson
        Lesson updatedLesson = new Lesson();
        updatedLesson.setId(1L);
        updatedLesson.setTitle("Обновленный урок");
        updatedLesson.setActive(false);

        when(lessonBuilderService.updateLesson(eq(1L), any(LessonBuilderRequest.class)))
                .thenReturn(updatedLesson);

        mockMvc.perform(put("/api/lesson-builder/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLessonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновленный урок"));

        System.out.println("✅ Шаг 2: Урок обновлен");

        // Step 3: Publish lesson
        Lesson publishedLesson = new Lesson();
        publishedLesson.setId(1L);
        publishedLesson.setTitle("Обновленный урок");
        publishedLesson.setActive(true);

        doNothing().when(lessonBuilderService).publishLesson(1L);

        mockMvc.perform(put("/api/lesson-builder/1/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        System.out.println("✅ Шаг 3: Урок опубликован");

        // Step 4: Get published lessons
        List<Lesson> publishedLessons = Arrays.asList(publishedLesson);
        when(lessonBuilderService.getPublishedLessons(testTeacher.getId()))
                .thenReturn(publishedLessons);

        mockMvc.perform(get("/api/lesson-builder/teacher/" + testTeacher.getId() + "/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].active").value(true));

        System.out.println("✅ Шаг 4: Опубликованные уроки получены");

        System.out.println("🎉 Полный workflow протестирован успешно!");
    }
} 