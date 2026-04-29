package ict.bean;

import java.time.LocalDateTime;

public class Notification {
    private final int id;
    private final int userId;
    private final String type;
    private final String message;
    private final LocalDateTime createdAt;
    private boolean read;

    public Notification(int id, int userId, String type, String message) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }

    public Notification(int id, int userId, String type, String message, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}


