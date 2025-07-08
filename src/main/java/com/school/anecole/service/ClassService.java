package com.school.anecole.service;

import com.school.anecole.model.Class;
import com.school.anecole.model.User;
import com.school.anecole.repository.ClassRepository;
import com.school.anecole.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassService {
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
    
    public Class getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }
    
    public List<Class> getClassesByTeacher(Long teacherId) {
        return classRepository.findByTeacherId(teacherId);
    }
    
    public List<Class> getClassesByStudent(Long studentId) {
        return classRepository.findByStudentsId(studentId);
    }
    
    public Class createClass(Class classEntity) {
        // Проверяем, что учитель существует
        if (classEntity.getTeacher() != null && classEntity.getTeacher().getId() != null) {
            User teacher = userRepository.findById(classEntity.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + classEntity.getTeacher().getId()));
            classEntity.setTeacher(teacher);
        }
        
        return classRepository.save(classEntity);
    }
    
    public Class updateClass(Class classEntity) {
        if (!classRepository.existsById(classEntity.getId())) {
            throw new RuntimeException("Class not found with id: " + classEntity.getId());
        }
        
        // Проверяем, что учитель существует
        if (classEntity.getTeacher() != null && classEntity.getTeacher().getId() != null) {
            User teacher = userRepository.findById(classEntity.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + classEntity.getTeacher().getId()));
            classEntity.setTeacher(teacher);
        }
        
        return classRepository.save(classEntity);
    }
    
    public void deleteClass(Long id) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with id: " + id);
        }
        classRepository.deleteById(id);
    }
    
    public Class addStudentToClass(Long classId, Long studentId) {
        Class classEntity = getClassById(classId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        if (!classEntity.getStudents().contains(student)) {
            classEntity.getStudents().add(student);
            return classRepository.save(classEntity);
        }
        
        return classEntity;
    }
    
    public Class removeStudentFromClass(Long classId, Long studentId) {
        Class classEntity = getClassById(classId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        classEntity.getStudents().remove(student);
        return classRepository.save(classEntity);
    }
    
    public List<User> getStudentsInClass(Long classId) {
        Class classEntity = getClassById(classId);
        return classEntity.getStudents();
    }
} 