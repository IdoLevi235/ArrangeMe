package com.example.arrangeme.Entities;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;

public class Task {
    TaskCategory category;
    String description;
    Uri photo;
    ReminderType reminderType;
    String location;


    public Task(TaskCategory category, String description, Uri photo, ReminderType reminderType, String location) {
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.reminderType = reminderType;
        this.location = location;
    }

    public Task() {

    }

    public TaskCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Uri getPhoto() {
        return photo;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public String getLocation() {
        return location;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}

