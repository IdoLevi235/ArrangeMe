package com.example.arrangeme;

public class User {
    public String email;
    public String password;
    public String fname;
    public String lname;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String fname, String lname) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }
}

