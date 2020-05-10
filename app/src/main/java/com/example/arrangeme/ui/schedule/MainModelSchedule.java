package com.example.arrangeme.ui.schedule;

import android.widget.Button;
import android.widget.ImageView;

public class MainModelSchedule {
    private String category;
    private String description;
    private String time;
    private String type;
    private String createDate;

    public MainModelSchedule(String category, String description, String time, String type, String createDate) {
        this.category = category;
        this.description = description;
        this.time = time;
        this.type = type;
        this.createDate = createDate;
    }

    public MainModelSchedule() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }




}


