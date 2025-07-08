package com.school.anecole.dto;

public class UserStats {
    private Long totalUsers;
    private Long activeUsers;
    private Long students;
    private Long teachers;
    private Long admins;
    
    public UserStats() {}
    
    public UserStats(Long totalUsers, Long activeUsers, Long students, Long teachers, Long admins) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.students = students;
        this.teachers = teachers;
        this.admins = admins;
    }
    
    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public Long getStudents() {
        return students;
    }
    
    public void setStudents(Long students) {
        this.students = students;
    }
    
    public Long getTeachers() {
        return teachers;
    }
    
    public void setTeachers(Long teachers) {
        this.teachers = teachers;
    }
    
    public Long getAdmins() {
        return admins;
    }
    
    public void setAdmins(Long admins) {
        this.admins = admins;
    }
} 