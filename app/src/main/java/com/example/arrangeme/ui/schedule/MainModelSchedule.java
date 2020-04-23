package com.example.arrangeme.ui.schedule;

public class MainModelSchedule {
    String category;
    String description;

    public MainModelSchedule() {

    }


    public MainModelSchedule(String category, String description) {
        this.category = category;
        this.description = description;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
