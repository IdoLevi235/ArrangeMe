package com.example.arrangeme;

import java.util.Vector;

public class User {
    public static String uid;
    public String email;
    public String password;
    public String fname;
    public String lname;
    public Vector<Integer> personality_vector = new Vector<Integer>();


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

