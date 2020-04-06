package com.example.arrangeme;

import java.util.Vector;

public class User {
    public String email;
    public String password;
    public String fname;
    public String lname;
    public Vector<Integer> personality_vector = new Vector<Integer>();


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String fname, String lname) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.personality_vector.setSize(25);
        this.personality_vector.add(1,3);
        this.personality_vector.add(7,5);

    }
}

