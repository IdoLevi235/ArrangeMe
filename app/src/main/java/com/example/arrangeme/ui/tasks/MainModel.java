package com.example.arrangeme.ui.tasks;

public class MainModel {
    Integer catLogo;
    String catName;

    public MainModel(Integer catLogo,String catName){
        this.catLogo=catLogo;
        this.catName=catName;
    }

    public Integer getCatLogo() {
        return catLogo;
    }

    public String getCatName() {
        return catName;
    }
}
