package com.example.arrangeme;


import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * static server functions
 */
public class Server {
    public boolean isDone;


    /**
     * filling questionnaire with Question q and Answer ans
     * @param q
     * @param ans
     */
    public static void questionnaireFill(String q, int ans){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(Globals.UID).child("personality_vector").child(q).setValue(ans);

    }


}

