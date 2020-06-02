package com.example.arrangeme.Entities;

import androidx.annotation.NonNull;

public class ScheduleItem{
    String hour;
    Boolean isWithTask;
    String startTime;
    String endTime;
    String category;
    String type;
    String anchorID;
    String createDate;
    String date;
    String description;
    int id;
    String location;
    String photoUri;
    String reminderType;
    Long idForCalendar;

    public ScheduleItem(String anchorID, String type, Long idForCalendar) {
        this.anchorID = anchorID;
        this.type = type;
        this.idForCalendar=idForCalendar;
    }


    public ScheduleItem(String startTime, String endTime, String category, String type, String date, String description, String location, Long idForCalendar) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
        this.date = date;
        this.description = description;
        this.location = location;
        this.idForCalendar=idForCalendar;
    }


    public ScheduleItem(String startTime, String endTime, String category, String type, String date, String description, String location, int id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
        this.date = date;
        this.description = description;
        this.location = location;
        this.id=id;
    }




    public ScheduleItem(String hour, Boolean isWithTask) {
        this.hour = hour;
        this.isWithTask = isWithTask;
    }

    public ScheduleItem(String startTime, String endTime, String category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }

    public ScheduleItem() {

    }

    public ScheduleItem(String startTime, String endTime, String category, String type) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
    }

    public ScheduleItem(String startTime, String endTime, String category, String type, String anchorID) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
        if(anchorID!=null)
            this.anchorID = anchorID;
        else this.anchorID="-1";
    }

    public ScheduleItem(String startTime, String endTime, String category, String type, String createDate, String description, String location) {
        this.category = category;
        this.type = type;
        this.createDate = createDate;
        this.description = description;
        this.location = location;
    }

    public ScheduleItem(String category, String createDate, String description, String location, String photoUri, String reminderType) {
        this.category = category;
        this.createDate = createDate;
        this.description = description;
        this.location = location;
        this.photoUri = photoUri;
        this.reminderType = reminderType;
    }


    public Long getIdForCalendar() {
        return idForCalendar;
    }

    public void setIdForCalendar(Long idForCalendar) {
        this.idForCalendar = idForCalendar;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAnchorID() {
        return anchorID;
    }

    public void setAnchorID(String anchorID) {
        this.anchorID = anchorID;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Boolean getWithTask() {
        return isWithTask;
    }

    public void setWithTask(Boolean withTask) {
        isWithTask = withTask;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        String sTime = this.getStartTime();
        String eTime = this.getEndTime();
        String category = this.getCategory();
        String description = this.getDescription();
        String createDate = this.getCreateDate();
        String location = this.getLocation();
        String ancorID =this.getAnchorID();
        long Id= this.getIdForCalendar();
        String type = this.getType();
        return "Start Time: " + sTime+ " End Time: " + eTime + " Category: " + category + " Type:" + type +" Description:" +description+" CreateDate:"+createDate+" Location:"+location+" AncorID:"+ancorID+" id for calendar:"+Id;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }
}
