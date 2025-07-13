package com.school.anecole.repository;

import com.school.anecole.model.Class;
import com.school.anecole.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    List<Lesson> findByClassEntity(Class classEntity);
    
    List<Lesson> findByClassEntityAndActive(Class classEntity, boolean active);
    
    List<Lesson> findByClassEntityOrderByOrderNumber(Class classEntity);
    
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.id = :classId AND l.active = true ORDER BY l.orderNumber")
    List<Lesson> findActiveByClassIdOrderByOrderNumber(@Param("classId") Long classId);
    
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.teacher.id = :teacherId AND l.active = true")
    List<Lesson> findActiveByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT l FROM Lesson l JOIN l.classEntity c JOIN c.students s WHERE s.id = :studentId AND l.active = true")
    List<Lesson> findActiveByStudentId(@Param("studentId") Long studentId);
    
    List<Lesson> findByClassEntity_Teacher_IdAndActiveOrderByCreatedAtDesc(Long teacherId, boolean active);
    
    List<Lesson> findByClassEntity_Students_IdAndActiveOrderByOrderNumberAsc(Long studentId, boolean active);
    
    // Новые методы для LessonBuilderService
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.teacher.id = :teacherId AND l.active = :active")
    List<Lesson> findByTeacherIdAndActive(@Param("teacherId") Long teacherId, @Param("active") boolean active);
    
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.id = :classId")
    List<Lesson> findByClassId(@Param("classId") Long classId);
    
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.id = :classId AND l.active = :active")
    List<Lesson> findByClassIdAndActive(@Param("classId") Long classId, @Param("active") boolean active);
    
    // Методы для поиска
    @Query("SELECT l FROM Lesson l WHERE l.title LIKE %:query% OR l.description LIKE %:query%")
    List<Lesson> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(@Param("query") String query);
    
    @Query("SELECT l FROM Lesson l WHERE l.classEntity.id = :classId AND (l.title LIKE %:query% OR l.description LIKE %:query%)")
    List<Lesson> findByClassIdAndTitleContainingIgnoreCaseOrClassIdAndDescriptionContainingIgnoreCase(
            @Param("classId") Long classId, @Param("query") String query);
} 