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

  public static String fromEnumToString (TaskCategory s){
    String t;
    switch (s){
      case STUDY:
        t="study";
        break;
      case SPORT:
        t="sport";
        break;
      case WORK:
        t="work";
        break;
      case NUTRITION:
        t="nutrition";
        break;
      case FAMILY:
        t="family";
        break;
      case CHORES:
        t="chores";
        break;
      case RELAX:
        t="relax";
        break;
      case FRIENDS:
        t="friends";
        break;
      case OTHER:
        t="other";
        break;
      default:
        t="none";
        break;
    }
    return t;
  }
  public static String fromIntToString (int x){
    String t;
    switch (x){
      case 0:
        t="study";
        break;
      case 1:
        t="sport";
        break;
      case 2:
        t="work";
        break;
      case 3:
        t="nutrition";
        break;
      case 4:
        t="family";
        break;
      case 5:
        t="chores";
        break;
      case 6:
        t="relax";
        break;
      case 7:
        t="friends";
        break;
      case 8:
        t="other";
        break;
      default:
        t=null;
        break;
    }
    return t;
  }

}
