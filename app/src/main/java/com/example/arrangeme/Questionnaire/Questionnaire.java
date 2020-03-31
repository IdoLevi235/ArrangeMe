package com.example.arrangeme.Questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.arrangeme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Questionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
    }
}

// TODO: What happens when user exits in the middle ? or leaving field empty?
// TODO: progress bar? dots?
// todo initial popup welcome
// TODO: לסדר את כל המלבנים עם התמונות הקטנות שיעבדו טוב כמו במסך הראשון
