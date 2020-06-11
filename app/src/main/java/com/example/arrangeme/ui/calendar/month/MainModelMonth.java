package com.example.arrangeme.ui.calendar.month;

public class MainModelMonth {
    private String category;
    private String description;
    private String type;
    private String createDate;
    private String startTime;
    private String endTime;
    private String AnchorID;
    private String activeKey;


    private String photoUri;

    public MainModelMonth(String category, String description, String time, String type, String creteDate) {
        this.category = category;
        this.description = description;
        this.type = type;
        this.createDate=creteDate;
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getActiveKey() {
        return activeKey;
    }

    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
    }
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

}

