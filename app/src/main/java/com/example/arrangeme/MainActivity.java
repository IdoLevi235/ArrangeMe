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
    Button mButton;
    private FirebaseAuth mAuth;
    Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        mButton = (Button) findViewById(R.id.test_button);
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
//TEST DB BUTTON onClick
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
        updateUI(currentUser);//TODO: what happens in UI when user opens mainactivity
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
//
