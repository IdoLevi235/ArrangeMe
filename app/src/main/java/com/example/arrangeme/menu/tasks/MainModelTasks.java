
package com.example.arrangeme.menu.tasks;

/**
 * Main odel of the tasks recycler view, taking information for FirebaseUI
 */
public class MainModelTasks {
    private int catBackgroundFull;
    private String category;
    private String description;
    private int catIcon;
    private int catBackground;


    private int color;
    private String photoUri;

    public MainModelTasks() {
    }

    public MainModelTasks(int catBackgroundFull, String category, String description, int catIcon, int catBackground, int color) {
        this.catBackgroundFull = catBackgroundFull;
        this.category = category;
        this.description = description;
        this.catIcon = catIcon;
        this.catBackground = catBackground;
        this.color = color;
    }


    public MainModelTasks(int catBackgroundFull, String category, String description, int catIcon, int catBackground) {
        this.catBackgroundFull = catBackgroundFull;
        this.category = category;
        this.description = description;
        this.catIcon = catIcon;
        this.catBackground = catBackground;
        this.color = color;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}