
package com.example.arrangeme.menu.tasks;

public class MainModelTasks {
    private int catBackgroundFull;
    private String category;
    private String description;
    private int catIcon;
    private int catBackground;

    public MainModelTasks() {
    }

    public MainModelTasks(int catBackgroundFull, String category, String description, int catIcon, int catBackground) {
        this.catBackgroundFull = catBackgroundFull;
        this.category = category;
        this.description = description;
        this.catIcon = catIcon;
        this.catBackground = catBackground;
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
}