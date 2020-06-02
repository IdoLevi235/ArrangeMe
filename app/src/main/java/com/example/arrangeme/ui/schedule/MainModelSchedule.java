package com.example.arrangeme.ui.schedule;

import android.widget.Button;
import android.widget.ImageView;

public class MainModelSchedule {
    private String category;
    private String description;
    private String type;
    private String startTime;
    private String endTime;


    public MainModelSchedule(String category, String description, String type, String startTime, String endTime) {
        this.category = category;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MainModelSchedule() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}


