package com.example.arrangeme.Enums;

public enum TaskCategory {
    STUDY, SPORT, WORK, NUTRITION, FAMILY, CHORES, RELAX, OTHER;

    public static TaskCategory fromInt (int x){
        TaskCategory t;
        switch (x){
            case 0:
                t=STUDY;
                break;
            case 1:
                t=SPORT;
                break;
            case 2:
                t=WORK;
                break;
            case 3:
                t=NUTRITION;
                break;
            case 4:
                t=FAMILY;
                break;
            case 5:
                t=CHORES;
                break;
            case 6:
                t=RELAX;
                break;
            case 7:
                t=OTHER;
                break;
            default:
                t=null;
                break;
        }
        return t;
    }


}
