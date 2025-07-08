package com.school.anecole.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class LessonBuilderRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Class ID is required")
    private Long classId;
    
    private Integer orderNumber;
    private String videoUrl;
    private String audioUrl;
    private String imageUrl;
    private List<WordRequest> words;
    private List<Long> studentIds;
    private Boolean publishImmediately = false;
    
    // Constructors
    public LessonBuilderRequest() {}
    
    public LessonBuilderRequest(String title, String description, Long classId) {
        this.title = title;
        this.description = description;
        this.classId = classId;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getClassId() {
        return classId;
    }
    
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    
    public Integer getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getAudioUrl() {
        return audioUrl;
    }
    
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public List<WordRequest> getWords() {
        return words;
    }
    
    public void setWords(List<WordRequest> words) {
        this.words = words;
    }
    
    public List<Long> getStudentIds() {
        return studentIds;
    }
    
    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }
    
    public Boolean getPublishImmediately() {
        return publishImmediately;
    }
    
    public void setPublishImmediately(Boolean publishImmediately) {
        this.publishImmediately = publishImmediately;
    }
    
    // Inner class for word requests
    public static class WordRequest {
        @NotBlank(message = "Word is required")
        private String word;
        
        @NotBlank(message = "Translation is required")
        private String translation;
        
        private String example;
        private String pronunciation;
        private String audioUrl;
        private String imageUrl;
        
        // Constructors
        public WordRequest() {}
        
        public WordRequest(String word, String translation) {
            this.word = word;
            this.translation = translation;
        }
        
        // Getters and Setters
        public String getWord() {
            return word;
        }
        
        public void setWord(String word) {
            this.word = word;
        }
        
        public String getTranslation() {
            return translation;
        }
        
        public void setTranslation(String translation) {
            this.translation = translation;
        }
        
        public String getExample() {
            return example;
        }
        
        public void setExample(String example) {
            this.example = example;
        }
        
        public String getPronunciation() {
            return pronunciation;
        }
        
        public void setPronunciation(String pronunciation) {
            this.pronunciation = pronunciation;
        }
        
        public String getAudioUrl() {
            return audioUrl;
        }
        
        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }
        
        public String getImageUrl() {
            return imageUrl;
        }
        
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
} 