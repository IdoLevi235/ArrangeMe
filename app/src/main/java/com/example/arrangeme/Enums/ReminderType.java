package com.example.arrangeme.Enums;

public enum ReminderType {
    FIVE_MIN,FIFTEEN_MIN,ONE_HOUR,ONE_DAY,NONE;

    public static ReminderType fromInt(int x){
        ReminderType t;
        switch(x){
            case 1:
                t=FIVE_MIN;
                break;
            case 2:
                t=FIFTEEN_MIN;
                break;
            case 3:
                t=ONE_HOUR;
                break;
            case 4:
                t=ONE_DAY;
                break;
            case 0:
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
                t=1;
                break;
            case "FIFTEEN_MIN":
                t=2;
                break;
            case "ONE_HOUR":
                t=3;
                break;
            case "ONE_DAY":
                t=4;
                break;
            case "NONE":
                t=0;
            default:
                t=-1;
                break;
        }
        return t;
    }


    public static String betterString(String s) {
            String t;
            switch (s){
            case "FIVE_MIN":
                t="5 min before";
                break;
            case "FIFTEEN_MIN":
                t="15 min before";
                break;
            case "ONE_HOUR":
                t="1 hour before";
                break;
            case "ONE_DAY":
                t="1 day before";
                break;
            case "NONE":
                t="None";
            default:
                t="";
                break;
        }
        return t;

    }
}
