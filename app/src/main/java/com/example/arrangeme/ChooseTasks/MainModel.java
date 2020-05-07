package com.example.arrangeme.ChooseTasks;

public class MainModel {
    private String category;
    private String description;

    public MainModel() {
    }

    public MainModel(String category, String description) {
        this.category = category;
        this.description = description;
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
}
