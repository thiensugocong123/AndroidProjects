package com.example.musicbook.ui.model;

public class Messages {
    private String message, sender;
    long timestamp;
    String currenttime;

    public Messages() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public Messages(String message, String sender, long timestamp, String currenttime) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
    }
}
