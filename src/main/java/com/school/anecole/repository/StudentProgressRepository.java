package com.school.anecole.repository;

import com.school.anecole.model.Lesson;
import com.school.anecole.model.StudentProgress;
import com.school.anecole.model.User;
import com.school.anecole.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    
    List<StudentProgress> findByStudent(User student);
    
    List<StudentProgress> findByStudentAndLesson(User student, Lesson lesson);
    
    List<StudentProgress> findByStudentAndCompleted(User student, boolean completed);
    
    Optional<StudentProgress> findByStudentAndWord(User student, Word word);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.student.id = :studentId AND sp.lesson.id = :lessonId")
    List<StudentProgress> findByStudentIdAndLessonId(@Param("studentId") Long studentId, @Param("lessonId") Long lessonId);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.student.id = :studentId AND sp.completed = true")
    List<StudentProgress> findCompletedByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT COUNT(sp) FROM StudentProgress sp WHERE sp.student.id = :studentId AND sp.completed = true")
    Long countCompletedByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT AVG(sp.score) FROM StudentProgress sp WHERE sp.student.id = :studentId")
    Double getAverageScoreByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.lesson.id = :lessonId ORDER BY sp.score DESC")
    List<StudentProgress> findByLessonIdOrderByScoreDesc(@Param("lessonId") Long lessonId);
} 