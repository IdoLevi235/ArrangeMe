package com.example.arrangeme.Questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.arrangeme.R;

public class Questionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
    }
}

//TODO: What happens when user exits in the middle ? or leaving field empty?
//TODO: progress bar? dots?
//TODO: username in top of each fragment
//todo: Start after sign up
//todo initial popup welcome
//TODO: apps question maybe CHIPGROUP