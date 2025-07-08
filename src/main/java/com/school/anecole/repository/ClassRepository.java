package com.school.anecole.repository;

import com.school.anecole.model.Class;
import com.school.anecole.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    
    List<Class> findByTeacher(User teacher);
    
    List<Class> findByTeacherAndActive(User teacher, boolean active);
    
    List<Class> findByGrade(Integer grade);
    
    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.id = :studentId")
    List<Class> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT c FROM Class c WHERE c.active = true")
    List<Class> findAllActive();
    
    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId AND c.active = true")
    List<Class> findActiveByTeacherId(@Param("teacherId") Long teacherId);
    
    // Новые методы для ClassService
    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId")
    List<Class> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.id = :studentId")
    List<Class> findByStudentsId(@Param("studentId") Long studentId);
} 