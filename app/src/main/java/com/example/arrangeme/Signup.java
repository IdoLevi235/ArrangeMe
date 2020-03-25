package com.example.arrangeme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private EditText confPassText;
    private EditText fnameText;
    private EditText lnameText;
    private Button sumbitBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sumbitBtn = (Button)findViewById(R.id.sumbitBtn);
        emailText=(EditText)findViewById(R.id.emailText);
        passwordText=(EditText)findViewById(R.id.passwordText);
        confPassText=(EditText)findViewById(R.id.confPasswordText);
        fnameText=(EditText)findViewById(R.id.firstNameText);
        lnameText=(EditText)findViewById(R.id.lastNameText);
        sumbitBtn.setOnClickListener(this);

    }

    protected void onStart() {
        super.onStart();
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

    @Override
    public void onClick(View v) {
        if (v==sumbitBtn){//works
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String confPass = passwordText.getText().toString();
            final String fname = fnameText.getText().toString(); // final because access from inner class
            String lname = lnameText.getText().toString();

            createAccount(email,password,fname); //TODO: EMAIL authentication with link (not important)
            addNewUserToDB(email,password,fname,lname);//maybe without email+password?
            //Intent intent = new Intent(Signup.this, Questionnaire.class); //Start the activity of Questionnaire
            //startActivity(intent);
        }
    }

    private void addNewUserToDB(String email, String password, String fname, String lname) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(email,password,fname,lname);
        mDatabase.push().setValue(user);
    }

    private void createAccount(String email, String password, final String fname) { //TODO: form validation (valid email, pass=confpass...)
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createAccountMail", "createUserWithEmail:success");
                            Toast toast=Toast. makeText(getApplicationContext(),"Registeration completed successfully!",Toast. LENGTH_SHORT);
                            toast. show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fname).build();
                            user.updateProfile(profileUpdates);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createAccountMail", "createUserWithEmail:failure", task.getException());
                            Toast toast=Toast. makeText(getApplicationContext(),"Registered failed!",Toast. LENGTH_SHORT);
                            toast. show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }
}
