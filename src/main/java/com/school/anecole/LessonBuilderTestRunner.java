package com.school.anecole;

import com.school.anecole.dto.LessonBuilderRequest;
import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import com.school.anecole.model.User;
import com.school.anecole.model.UserRole;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.LessonRepository;
import com.school.anecole.repository.UserRepository;
import com.school.anecole.service.LessonBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Тестовый раннер для конструктора уроков
 * Запускается только в профиле "test-runner"
 * 
 * Использование:
 * mvn spring-boot:run -Dspring-boot.run.profiles=test-runner
 */
@Component
public class LessonBuilderTestRunner {

    @Bean
    @Profile("test-runner")
    public CommandLineRunner testRunner(
            @Autowired LessonBuilderService lessonBuilderService,
            @Autowired UserRepository userRepository,
            @Autowired ClassRepository classRepository,
            @Autowired LessonRepository lessonRepository,
            @Autowired PasswordEncoder passwordEncoder) {
        
        return args -> {
            System.out.println("🧪 Запуск тестирования API конструктора уроков...\n");
            
            try {
                // Очистка базы данных
                lessonRepository.deleteAll();
                classRepository.deleteAll();
                userRepository.deleteAll();
                
                // 1. Создание тестовых данных
                System.out.println("1. Создание тестовых данных...");
                User teacher = createTeacher(userRepository, passwordEncoder);
                User student1 = createStudent(userRepository, passwordEncoder, "student1", "student1@example.com");
                User student2 = createStudent(userRepository, passwordEncoder, "student2", "student2@example.com");
                Class testClass = createClass(classRepository, teacher, Arrays.asList(student1, student2));
                
                System.out.println("✅ Тестовые данные созданы");
                
                // 2. Создание урока
                System.out.println("\n2. Создание урока...");
                LessonBuilderRequest testRequest = createTestLessonRequest(testClass.getId(), 
                    Arrays.asList(student1.getId(), student2.getId()));
                
                Lesson createdLesson = lessonBuilderService.createLessonFromBuilder(testRequest);
                System.out.println("✅ Урок создан с ID: " + createdLesson.getId());
                
                // 3. Получение черновиков
                System.out.println("\n3. Получение черновиков...");
                List<Lesson> drafts = lessonBuilderService.getDraftLessonsByTeacher(teacher.getId());
                System.out.println("✅ Черновики получены: " + drafts.size() + " уроков");
                
                // 4. Обновление урока
                System.out.println("\n4. Обновление урока...");
                testRequest.setTitle("Обновленный тестовый урок");
                testRequest.setOrderNumber(2);
                Lesson updatedLesson = lessonBuilderService.updateLessonFromBuilder(createdLesson.getId(), testRequest);
                System.out.println("✅ Урок обновлен: " + updatedLesson.getTitle());
                
                // 5. Публикация урока
                System.out.println("\n5. Публикация урока...");
                lessonBuilderService.publishLessonForStudents(createdLesson.getId(), 
                    Arrays.asList(student1.getId(), student2.getId()));
                System.out.println("✅ Урок опубликован");
                
                // 6. Получение опубликованных уроков
                System.out.println("\n6. Получение опубликованных уроков...");
                List<Lesson> published = lessonBuilderService.getPublishedLessonsByTeacher(teacher.getId());
                System.out.println("✅ Опубликованные уроки получены: " + published.size() + " уроков");
                
                // 7. Получение уроков для ученика
                System.out.println("\n7. Получение уроков для ученика...");
                List<Lesson> studentLessons = lessonBuilderService.getLessonsForStudent(student1.getId());
                System.out.println("✅ Уроки для ученика получены: " + studentLessons.size() + " уроков");
                
                // 8. Копирование урока
                System.out.println("\n8. Копирование урока...");
                Class targetClass = createClass(classRepository, teacher, Arrays.asList(student1));
                Lesson copiedLesson = lessonBuilderService.copyLessonToClass(createdLesson.getId(), targetClass.getId());
                System.out.println("✅ Урок скопирован с ID: " + copiedLesson.getId());
                
                // 9. Удаление урока
                System.out.println("\n9. Удаление урока...");
                lessonBuilderService.deleteLessonWithWords(createdLesson.getId());
                System.out.println("✅ Урок удален");
                
                System.out.println("\n🎉 Все тесты прошли успешно!");
                
            } catch (Exception e) {
                System.err.println("❌ Ошибка тестирования: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
    
    private User createTeacher(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User teacher = new User();
        teacher.setUsername("testteacher");
        teacher.setEmail("teacher@example.com");
        teacher.setPassword(passwordEncoder.encode("password123"));
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setRole(UserRole.TEACHER);
        teacher.setSubject("English");
        return userRepository.save(teacher);
    }
    
    private User createStudent(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                             String username, String email) {
        User student = new User();
        student.setUsername(username);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode("password123"));
        student.setFirstName("Student");
        student.setLastName(username.substring(8)); // Extract number from username
        student.setRole(UserRole.STUDENT);
        student.setGrade(10);
        return userRepository.save(student);
    }
    
    private Class createClass(ClassRepository classRepository, User teacher, List<User> students) {
        Class testClass = new Class();
        testClass.setName("Test Class");
        testClass.setDescription("Test class description");
        testClass.setGrade(10);
        testClass.setTeacher(teacher);
        testClass.setStudents(students);
        return classRepository.save(testClass);
    }
    
    private LessonBuilderRequest createTestLessonRequest(Long classId, List<Long> studentIds) {
        LessonBuilderRequest request = new LessonBuilderRequest();
        request.setTitle("Тестовый урок");
        request.setDescription("Описание тестового урока");
        request.setClassId(classId);
        request.setOrderNumber(1);
        request.setVideoUrl("https://example.com/video.mp4");
        request.setAudioUrl("https://example.com/audio.mp3");
        request.setImageUrl("https://example.com/image.jpg");
        request.setStudentIds(studentIds);
        request.setPublishImmediately(false);
        
        // Добавляем слова
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
        
        request.setWords(Arrays.asList(word1, word2));
        
        return request;
    }
} 