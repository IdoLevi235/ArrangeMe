package com.example.arrangeme.BuildSchedule;

/**
 * This class helps get and set the current information from Firebase database using FirebaseUI
 */
public class MainModel {
    private String category;
    private String description;
    private int color;

    public MainModel() {
    }

    public MainModel(String category, String description, int color) {
        this.category = category;
        this.description = description;
        this.color = color;
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


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
