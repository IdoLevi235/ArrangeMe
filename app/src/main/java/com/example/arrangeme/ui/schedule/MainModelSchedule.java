package com.example.arrangeme.ui.schedule;

public class MainModelSchedule {
    String name;


    Integer icon;
    public MainModelSchedule(String name, Integer icon) {
        this.name = name;
        this.icon=icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

}
