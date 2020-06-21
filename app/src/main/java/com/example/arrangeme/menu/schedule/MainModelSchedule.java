package com.example.arrangeme.menu.schedule;

/**
 * Main model of schedule recycler, taking information directly from FirebaseUI
 */
public class MainModelSchedule {
    private String category;
    private String description;
    private String type;
    private String startTime;
    private String endTime;
    private String activeKey;


    private String photoUri;
    private String AnchorID;


    public MainModelSchedule(String category, String description, String type, String startTime, String endTime) {
        this.category = category;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MainModelSchedule() {
    }

    public String getActiveKey() {
        return activeKey;
    }

    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
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

    public String getAnchorID() {
        return AnchorID;
    }

    public void setAnchorID(String anchorID) {
        AnchorID = anchorID;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

}


