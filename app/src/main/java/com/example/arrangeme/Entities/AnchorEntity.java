package com.example.arrangeme.Entities;

import android.net.Uri;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;

import java.util.Calendar;

public class AnchorEntity {
    TaskCategory category;
    String description;
    Uri photo;
    ReminderType reminderType;
    String location;
    String date;
    String timeStart;
    String timeEnd;

    public AnchorEntity() {

    }

    public AnchorEntity(TaskCategory category, String description, Uri photo, ReminderType reminderType, String location, String date, String startTime, String endTime) {
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.reminderType = reminderType;
        this.location = location;
        this.date = date;
        this.timeStart = startTime;
        this.timeEnd = endTime;
    }


    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return timeStart;
    }

    public void setStartTime(String startTime) {
        this.timeStart = startTime;
    }

    public String getEndTime() {
        return timeEnd;
    }

    public void setEndTime(String endTime) {
        this.timeEnd = endTime;
    }
}
