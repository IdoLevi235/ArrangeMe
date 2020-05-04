package com.example.arrangeme.Entities;

import android.net.Uri;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;

import java.util.Calendar;

public class AnchorEntity {
    String category;
    String description;
    Uri photo;
    ReminderType reminderType;
    String location;
    String createDate;
    Calendar startDate;
    Calendar endDate;



    public AnchorEntity(String category, String description, Uri photo, ReminderType reminderType,String location, String createDate, Calendar startDate, Calendar endDate) {
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.reminderType = reminderType;
        this.location = location;
        this.createDate=createDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }



}
