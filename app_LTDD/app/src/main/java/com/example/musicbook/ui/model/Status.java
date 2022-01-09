package com.example.musicbook.ui.model;

import android.provider.MediaStore;

import java.util.Date;

public class Status {
    private String user_id;
    private String caption;
    private String image;
    private MediaStore.Audio audio;
    private String time;

    public Status( String user_id, String caption, String time) {
        this.user_id = user_id;
        this.caption = caption;
        this.time=time;
    }

    public Status(String user_id, String caption, String image, MediaStore.Audio audio, String time) {
        this.user_id = user_id;
        this.caption = caption;
        this.image = image;
        this.audio = audio;
        this.time = time;
    }

    public Status(String caption, String time) {
        this.caption = caption;
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MediaStore.Audio getAudio() {
        return audio;
    }

    public void setAudio(MediaStore.Audio audio) {
        this.audio = audio;
    }

    public Status() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}