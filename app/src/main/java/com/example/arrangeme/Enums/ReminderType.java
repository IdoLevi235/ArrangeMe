package com.example.arrangeme.Enums;

public enum ReminderType {
    FIVE_MIN,FIFTEEN_MIN,ONE_HOUR,ONE_DAY;

    public static ReminderType fromInt(int x){
        ReminderType t;
        switch(x){
            case 0:
                t=FIVE_MIN;
                break;
            case 1:
                t=FIFTEEN_MIN;
                break;
            case 2:
                t=ONE_HOUR;
                break;
            case 3:
                t=ONE_DAY;
                break;
            default:
                t=null;
        }
        return t;
    }
}
