package com.example.arrangeme;
//LINOY
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        signUpBtn = (Button) findViewById(R.id.signUpBtn);


//Login button onClick
        loginBtn.setOnClickListener((new View.OnClickListener() { //TODO: implement view.setonclick and make the code look nicer
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
        signUpBtn.setOnClickListener((new View.OnClickListener() { //TODO: implement view.setonclick and make the code look nicer
            @Override
            public void onClick(View v) {
                if (v==signUpBtn){
                    Log.d("signup", "signup Btn Activated");
                    Intent intent = new Intent(MainActivity.this, Signup.class);//
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
//TODO: dots indicator instead of progress bar?
//TODO: slide left right option
