package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

   Button loginBtn;
    private DatabaseReference mDatabase;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        mButton = (Button) findViewById(R.id.test_button);



        mButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==mButton){
                    Log.d("db", "database activated");
                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    User user = new User("a", "b");
                    mDatabase.push().setValue(user);
                    Toast toast = Toast. makeText(getApplicationContext(),"User added",Toast. LENGTH_SHORT);
                    toast.show();
                }

            }

        }));


        loginBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==loginBtn){
                    Log.d("login", "login Btn Activated");
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }

            }

        }));

    }


}
//
