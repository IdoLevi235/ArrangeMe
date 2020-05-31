package com.example.arrangeme.Entities;

import android.net.Uri;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;

import java.util.Calendar;

public class AnchorEntity{
    String category;
    String description;
    Uri photo;
    ReminderType reminderType;
    String location;
    String date;
    String timeStart;
    String timeEnd;

    public String getAnchorID() {
        return anchorID;
    }

    public void setAnchorID(String anchorID) {
        this.anchorID = anchorID;
    }

    String anchorID;
    public AnchorEntity() {

    }

    public AnchorEntity(String category, String description, Uri photo, ReminderType reminderType, String location, String date, String startTime, String endTime) {
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.reminderType = reminderType;
        this.location = location;
        this.date = date;
        this.timeStart = startTime;
        this.timeEnd = endTime;
    }

        public AnchorEntity(String timeStart, String timeEnd,String anchorID, String category) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.anchorID=anchorID;
        this.category = category;
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
