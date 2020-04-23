package com.example.arrangeme.AddTasks;

public class MainModel {
    Integer catBackground;
    Integer catLogo;
    String catName;
    Integer catColor;
    Integer catColorFull;


    public MainModel(Integer catLogo, String catName, Integer catBackground, Integer catColor,Integer catColorFull){
        this.catLogo=catLogo;
        this.catName=catName;
        this.catBackground = catBackground;
        this.catColor=catColor;
        this.catColorFull=catColorFull;
    }

    public MainModel(String check) {
    }

    public Integer getCatLogo() {
        return catLogo;
    }

    public String getCatName() {
        return catName;
    }

    public Integer getCatBackground() {
        return catBackground;
    }

    public Integer getCatColor() {
        return catColor;
    }

    public Integer getCatColorFull() {
        return catColorFull;
    }
}
