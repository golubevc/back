package com.school.anecole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_progress")
public class StudentProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;
    
    @NotNull
    private Integer score;
    
    @NotNull
    private Integer attempts;
    
    private LocalDateTime lastAttemptAt;
    
    private LocalDateTime completedAt;
    
    private boolean completed = false;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
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
    public StudentProgress() {}
    
    public StudentProgress(User student, Lesson lesson, Word word) {
        this.student = student;
        this.lesson = lesson;
        this.word = word;
        this.score = 0;
        this.attempts = 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public Lesson getLesson() {
        return lesson;
    }
    
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
    
    public Word getWord() {
        return word;
    }
    
    public void setWord(Word word) {
        this.word = word;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Integer getAttempts() {
        return attempts;
    }
    
    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
    
    public LocalDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }
    
    public void setLastAttemptAt(LocalDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed && completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
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
    
    // Helper methods
    public void incrementAttempts() {
        this.attempts++;
        this.lastAttemptAt = LocalDateTime.now();
    }
    
    public void updateScore(Integer newScore) {
        this.score = Math.max(this.score, newScore);
        this.lastAttemptAt = LocalDateTime.now();
    }
} 