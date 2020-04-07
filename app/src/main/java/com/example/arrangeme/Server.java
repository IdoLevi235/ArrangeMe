package com.example.arrangeme;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.Questionnaire.Screen19Q;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Server extends Activity {
    public boolean isDone;


    public static void questionnaireFill(String q, int ans){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(Globals.UID).child("personality_vector").child(q).setValue(ans);

    }

    public static void isQuestionnaireFilled() {
    }


}

