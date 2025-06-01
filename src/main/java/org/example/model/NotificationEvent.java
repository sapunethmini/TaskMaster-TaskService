package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class NotificationEvent {
    private String eventType;
    private Long userId;
    private String userEmail;
    private String title;
    private String message;
    private Long taskId;
    private String taskTitle;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public NotificationEvent() {
        this.timestamp = LocalDateTime.now();
    }
    
    public NotificationEvent(String eventType, Long userId, String userEmail, String title, String message) {
        this();
        this.eventType = eventType;
        this.userId = userId;
        this.userEmail = userEmail;
        this.title = title;
        this.message = message;
    }
    
    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    
    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
} 