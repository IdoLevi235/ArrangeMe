package com.example.arrangeme;

public class ScheduleItem {
    String hour;
    Boolean isTaken;

    public ScheduleItem(String hour, Boolean isTaken) {
        this.hour = hour;
        this.isTaken = isTaken;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Boolean getTaken() {
        return isTaken;
    }

    public void setTaken(Boolean taken) {
        isTaken = taken;
    }
}
