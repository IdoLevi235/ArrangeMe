package com.example.arrangeme.Questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Questionnaire extends AppCompatActivity {


    private int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Bundle b = getIntent().getExtras();
        if(b != null)
            value = b.getInt("isFromGoogle");
        if(value==1)
        {
            Globals.isFromGoogle=true;
        }
        else if(value==0)
        {
            Globals.isFromGoogle=false;
        }
    }


}

