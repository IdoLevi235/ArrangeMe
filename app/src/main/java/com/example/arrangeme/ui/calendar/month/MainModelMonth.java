package com.example.arrangeme.ui.calendar.month;

public class MainModelMonth {
    private String category;
    private String description;
    private String time;
    private String type;


    public MainModelMonth(String category, String description, String time, String type) {
        this.category = category;
        this.description = description;
        this.time = time;
        this.type = type;
    }

    public MainModelMonth() {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
