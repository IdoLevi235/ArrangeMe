package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button login;
    private ImageView avatar;
    private Button testButton;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        username = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.sumbitBtn);
        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == login) {
                    validate(username.getText().toString(), password.getText().toString());
                }
            }
        }));


    }

    private void validate(String username, String userpassword)
    {
        System.out.println(username+" "+userpassword);
        if (username.equals("a") && userpassword.equals("a")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }



}
