package com.example.arrangeme;

public class ScheduleItem {
    String hour;
    Boolean isWithTask;

    public ScheduleItem(String hour, Boolean isWithTask) {
        this.hour = hour;
        this.isWithTask = isWithTask;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    public Boolean getWithTask() {
        return isWithTask;
    }

    public void setWithTask(Boolean withTask) {
        isWithTask = withTask;
    }

}
