package com.example.arrangeme.Entities;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class that represents tasks entity in our code
 */
public class TaskEntity {
    TaskCategory category;
    String description;
    Uri photo;
    ReminderType reminderType;
    String location;
    String createDate;
    String categoryS;
    String reminderTypeS;
    String id;


    public TaskEntity(TaskCategory category, String description, Uri photo, ReminderType reminderType, String location, String createDate) {
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.reminderType = reminderType;
        this.location = location;
        this.createDate=createDate;

    }


    public TaskEntity(String categoryS, String description, Uri photo, String reminderTypeS, String location, String createDate, String id) {
        this.categoryS = categoryS;
        this.description = description;
        this.photo = photo;
        this.reminderTypeS = reminderTypeS;
        this.location = location;
        this.createDate=createDate;
        this.id=id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCategoryS() {
        return categoryS;
    }

    public void setCategoryS(String categoryS) {
        this.categoryS = categoryS;
    }

    public String getReminderTypeS() {
        return reminderTypeS;
    }

    public void setReminderTypeS(String reminderTypeS) {
        this.reminderTypeS = reminderTypeS;
    }

    public TaskEntity() {

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
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }





}

