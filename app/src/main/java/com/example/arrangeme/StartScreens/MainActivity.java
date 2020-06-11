package com.example.arrangeme.StartScreens;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.Globals;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Main activity of the project - takes to login/signup/directly to hoomepage if logged in
 */
public class MainActivity extends AppCompatActivity{

    Button loginBtn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Button signUpBtn;
    Button adminBtn;
    Button homepageBtn;
    private TextView tv;
    private ProgressBar pb;
    Context ctx;

    /**
     * this function controls what happens on creation of the activity
     * If user is already logged in - goes to homepage
     * else - regular main activity with login/signup/forgot password
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        adminBtn = (Button) findViewById(R.id.adminBtn);
        tv = (TextView)findViewById(R.id.textView3);
        pb = (ProgressBar)findViewById(R.id.progressBar4);
        pb.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) { // The user is already logged in , send him directly to homepage

            pb.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
            tv.setText("Please wait while we connect you to ArrangeMe");
            Globals.currentUsername = user.getDisplayName();
            Globals.currentEmail = user.getEmail();
            Globals.UID = user.getUid();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                    startActivity(intent);

                }
            },1500);
        }

        else {
            Toast.makeText(MainActivity.this, "not logged", Toast.LENGTH_SHORT).show();
        }

//Login button onClick
        loginBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==loginBtn){
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


    }
}
