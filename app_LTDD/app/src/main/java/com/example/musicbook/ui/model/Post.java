package com.example.musicbook.ui.model;

public class Post {
    private String content;
    private String time;
    private String userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Post(String content, String time) {
        this.content = content;
        this.time = time;
    }


}
