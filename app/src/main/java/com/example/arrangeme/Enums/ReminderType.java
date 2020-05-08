package com.example.arrangeme.Enums;

public enum ReminderType {
    FIVE_MIN,FIFTEEN_MIN,ONE_HOUR,ONE_DAY,NONE;

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
            case 4:
                t=NONE;
            default:
                t=null;
                break;
        }
        return t;
    }

    public static int fromStringToInt (String s){
        int t;
        switch (s){
            case "FIVE_MIN":
                t=0;
                break;
            case "FIFTEEN_MIN":
                t=1;
                break;
            case "ONE_HOUR":
                t=2;
                break;
            case "ONE_DAY":
                t=3;
                break;
            case "NONE":
                t=4;
            default:
                t=-1;
                break;
        }
        return t;
    }
}
