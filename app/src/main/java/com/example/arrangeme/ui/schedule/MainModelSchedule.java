package com.example.arrangeme.ui.schedule;

public class MainModelSchedule {
    private int catBackgroundFull;
    private String category;
    private String description;
    private int catIcon;
    private int catBackground;

    public MainModelSchedule() {

    }

    public MainModelSchedule(String category, String description, int catIcon, int catBackgroundFull, int catBackground) {
        this.category = category;
        this.description = description;
        this.catBackgroundFull=catBackgroundFull;
        this.catIcon=catIcon;
        this.catBackground= catBackground;
    }

    public int getCatIcon() {
        return catIcon;
    }

    public void setCatIcon(int catIcon) {
        this.catIcon = catIcon;
    }


    public int getCatBackgroundFull() {
        return catBackgroundFull;
    }



    public void setCatBackgroundFull(int catBackground) {
        this.catBackgroundFull = catBackground;
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

    public int getCatBackground() {
        return catBackgroundFull;
    }

    public void setCatBackground(int catBackground) {
        this.catBackground = catBackground;
    }





}


