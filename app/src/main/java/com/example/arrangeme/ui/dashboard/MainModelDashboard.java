package com.example.arrangeme.ui.dashboard;

import android.widget.Button;

public class MainModelDashboard {
    private int catBackgroundFull;
    private String category;
    private String description;
    private int catIcon;
    private int catBackground;
    private String time;
    private Button anchorOrTask;
    private String type;

    public MainModelDashboard() {
    }

    public int getCatBackgroundFull() {
        return catBackgroundFull;
    }

    public void setCatBackgroundFull(int catBackgroundFull) {
        this.catBackgroundFull = catBackgroundFull;
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

    public int getCatIcon() {
        return catIcon;
    }

    public void setCatIcon(int catIcon) {
        this.catIcon = catIcon;
    }

    public int getCatBackground() {
        return catBackground;
    }

    public void setCatBackground(int catBackground) {
        this.catBackground = catBackground;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Button getAnchorOrTask() {
        return anchorOrTask;
    }

    public void setAnchorOrTask(Button anchorOrTask) {
        this.anchorOrTask = anchorOrTask;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
