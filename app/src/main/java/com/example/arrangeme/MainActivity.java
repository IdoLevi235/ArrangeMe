package com.example.arrangeme;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity{

    Button loginBtn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Button signUpBtn;
    Button adminBtn;
    Button homepageBtn;
    private TextView tv;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtnMain);
        homepageBtn = (Button) findViewById(R.id.homepageBtn);
        homepageBtn.setVisibility(View.GONE);
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
