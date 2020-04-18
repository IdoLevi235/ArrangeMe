package com.example.arrangeme.AddTasks;

public class MainModel {
    Integer catBackground;
    Integer catLogo;
    String catName;

    public MainModel(Integer catLogo,String catName , Integer catBackground){
        this.catLogo=catLogo;
        this.catName=catName;
        this.catBackground = catBackground;
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
}
