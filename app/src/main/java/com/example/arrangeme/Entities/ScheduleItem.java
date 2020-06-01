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
    String description;
    String id;
    String location;

    public ScheduleItem(String anchorID, String type) {
        this.anchorID = anchorID;
        this.type = type;
    }


    public ScheduleItem(String startTime, String endTime, String category, String type, String createDate, String description, String location, String id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.type = type;
        this.createDate = createDate;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        String Id= this.getId();
        String type = this.getType();
        return "Start Time: " + sTime+ " End Time: " + eTime + " Category: " + category + " Type:" + type +" Description:" +description+" CreateDate:"+createDate+" Location:"+location+" AncorID:"+ancorID+" Id:"+Id;
    }
}
