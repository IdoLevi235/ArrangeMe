package com.example.arrangeme.Enums;

public enum TaskCategory {
  STUDY, SPORT, WORK, NUTRITION, FAMILY, CHORES, RELAX, FRIENDS, OTHER;

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
        t=FRIENDS;
        break;
      case 8:
        t=OTHER;
        break;
      default:
        t=null;
        break;
    }
    return t;
  }

  public static int fromStringToInt (String s){
    int t;
    switch (s){
      case "STUDY":
        t=0;
        break;
      case "SPORT":
        t=1;
        break;
      case "WORK":
        t=2;
        break;
      case "NUTRITION":
        t=3;
        break;
      case "FAMILY":
        t=4;
        break;
      case "CHORES":
        t=5;
        break;
      case "RELAX":
        t=6;
        break;
      case "FRIENDS":
        t=7;
        break;
      case "OTHER":
        t=8;
        break;
      default:
        t=-1;
        break;
    }
    return t;
  }

}
