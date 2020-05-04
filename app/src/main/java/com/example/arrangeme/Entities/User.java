package com.example.arrangeme.Entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.arrangeme.Globals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class User {
    public static String uid;
    public String email;
    public String password;
    public String fname;
    public String lname;
    public Vector<Integer> personality_vector = new Vector<Integer>();
    public DatabaseReference mDatabase;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String email, String fname) { //Special constructor for google sign-in
        this.email = email;
        this.fname = fname;
        this.personality_vector.setSize(25);
        for (int i=1;i<=25;i++){
            this.personality_vector.add(i,0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Calender").child("Year").child("Month").child("Day");

    }


    public User(String email, String password, String fname, String lname) {
        //this.uid = Globals.UID;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.personality_vector.setSize(25);
        for (int i=1;i<=25;i++){
            this.personality_vector.add(i,0);
        }
    }
}

