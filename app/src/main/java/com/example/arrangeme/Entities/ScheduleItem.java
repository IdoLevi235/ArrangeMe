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
        String type = this.getType();
        return "Start: " + sTime+ " End: " + eTime + " Category: " + category + " Type:" + type;
    }
}
