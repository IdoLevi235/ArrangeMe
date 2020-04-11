package com.example.arrangeme;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    Button loginBtn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Button signUpBtn;
    Button adminBtn;
    Button homepageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        homepageBtn = (Button) findViewById(R.id.homepageBtn);
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        adminBtn = (Button) findViewById(R.id.adminBtn);

//Login button onClick
        loginBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==loginBtn){
                    Log.d("login", "login Btn Activated");
                    Intent intent = new Intent(MainActivity.this, Login.class);//
                    startActivity(intent);
                }

            }
    }));

//Sign Up button clicked
        signUpBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==signUpBtn){
                    Log.d("signup", "signup Btn Activated");
                    Intent intent = new Intent(MainActivity.this, Signup.class);//
                    startActivity(intent);
                }

            }

        }));

        adminBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==adminBtn){
                    Intent intent = new Intent(MainActivity.this, Adminzone.class);//
                    startActivity(intent);
                }

            }

        }));


        homepageBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==homepageBtn){
                    Intent intent = new Intent(MainActivity.this, Homepage.class);//
                    startActivity(intent);
                }

            }

        }));



    }
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }


}
//TODO: night mode option
//TODO: change the "description"!!!!!!!!!!!
//TODO: slide left right option
