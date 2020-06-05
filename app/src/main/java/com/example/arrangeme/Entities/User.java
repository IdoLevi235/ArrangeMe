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

/**
 * Class that represents user in our code
 */
public class User {
    public static String uid;
    public String email;
    public String password;
    public String fname;
    public String lname;
    public DatabaseReference mDatabase;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String email, String fname) { //Special constructor for google sign-in
        this.email = email;
        this.fname = fname;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Calender").child("Year").child("Month").child("Day");
    }


    public User(String email, String password, String fname, String lname) {
        //this.uid = Globals.UID;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}

