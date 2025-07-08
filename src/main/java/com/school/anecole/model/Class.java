package com.school.anecole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
public class Class {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @NotBlank
    @Size(max = 500)
    private String description;
    
    private Integer grade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "classes_students",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();
    
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons = new ArrayList<>();
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean active = true;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Class() {}
    
    public Class(String name, String description, User teacher) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getGrade() {
        return grade;
    }
    
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    
    public User getTeacher() {
        return teacher;
    }
    
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
    
    public List<User> getStudents() {
        return students;
    }
    
    public void setStudents(List<User> students) {
        this.students = students;
    }
    
    public List<Lesson> getLessons() {
        return lessons;
    }
    
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Helper methods
    public void addStudent(User student) {
        if (!this.students.contains(student)) {
            this.students.add(student);
        }
    }
    
    public void removeStudent(User student) {
        this.students.remove(student);
    }
    
    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setClassEntity(this);
    }
    
    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setClassEntity(null);
    }
} 