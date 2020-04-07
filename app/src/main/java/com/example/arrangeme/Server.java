package com.example.arrangeme;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

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
        final ArrayList<Integer> q_answers = new ArrayList<>() ;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference currUserRef = mDatabase.child("users").child(Globals.UID).child("personality_vector");
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
                if (!q_answers.contains(0)) {
                    System.out.println("GOOD");
                }
                else {
                    System.out.println("KAKA");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

