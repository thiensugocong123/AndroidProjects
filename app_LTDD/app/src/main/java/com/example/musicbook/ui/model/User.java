package com.example.musicbook.ui.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class User {
    private String name,email, phone,cover, image, location,job,school,uid;

    public User(String name, String email, String phone, String cover, String image, String location, String job, String school, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.image = image;
        this.location = location;
        this.job = job;
        this.school = school;
        this.uid = uid;
    }

    public User(String name, String image, String location, String uid) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.uid = uid;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
//    @BindingAdapter("android:loadImage")
//    public static void loadImage(ImageView imageView,String imageurl){
//        Picasso.get().load(imageurl).into(imageView);
//    }
}

