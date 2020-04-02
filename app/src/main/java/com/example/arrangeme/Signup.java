package com.example.arrangeme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arrangeme.Questionnaire.Questionnaire;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

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


    protected void onDestroy() {
        super.onDestroy();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v==sumbitBtn){//works
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String confPass = confPassText.getText().toString();
            final String fname = fnameText.getText().toString(); // final because access from inner class
            String lname = lnameText.getText().toString();
            if (signUpFormValidation(email,password,confPass,fname,lname)) //form is OK
            {
                createAccount(email, password, fname); //TODO: EMAIL authentication with link (not important)
                addNewUserToDB(email, password, fname, lname);//maybe without email+password?
                Globals.currentUsername = fname;
                //loginAfterRegistartion(email,password);
            }

            //updateUI(user);

        }
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean signUpFormValidation(String email, String password, String confPass, String fname, String lname) {
        String msg;
        if (!password.equals(confPass)){
            msg = "Passwords doesn't match";
            createErrorAlert(msg);
            return false;
        }
        else if (email.isEmpty()||password.isEmpty()||confPass.isEmpty()||fname.isEmpty()||lname.isEmpty()) {
            msg = "You must fill all fields";
            createErrorAlert(msg);
            return false;
        }

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")

    private void createErrorAlert(String msg) {
       SweetAlertDialog ad;
       ad =  new SweetAlertDialog(Signup.this, SweetAlertDialog.ERROR_TYPE);
       ad.setTitleText("Error");
       ad.setContentText(msg);
       ad.show();
       Button btn = (Button) ad.findViewById(R.id.confirm_button);
       btn.setBackgroundResource(R.drawable.rounded_rec);
    }

    private void createWelcomeAlert(){
        SweetAlertDialog ad;
       ad =  new SweetAlertDialog(Signup.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Welcome :)")
                .setContentText(("You've registered successfully into ArrangeMe! In order to provide " +
                        "great schedule recommendations for you, we will ask you few questions " +
                        "and in that way we will get to know you better and adjust ourself according" +
                        " to your preferences."));
        ad.setConfirmText("Let's start!");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(Signup.this, Questionnaire.class));
                    }
                });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);


    }

    private void addNewUserToDB(String email, String password, String fname, String lname) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(email,password,fname,lname);
        mDatabase.push().setValue(user);
    }

    private void createAccount(String email, String password, final String fname) {
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createAccountMail", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fname).build();
                            user.updateProfile(profileUpdates);
                            createWelcomeAlert();
                            //startActivity(new Intent(Signup.this, Questionnaire.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createAccountMail", "createUserWithEmail:failure", task.getException());
                            createErrorAlert("Something went wrong, try again please!");
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }

    private void loginAfterRegistartion(String email, String password) {
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignInWithMail", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Connected successfully " + user.getDisplayName(),
                                        Toast.LENGTH_SHORT).show();
                                Globals.currentUsername = user.getDisplayName();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignInWithMail", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }//end of try
        catch(IllegalArgumentException e){
            Toast.makeText(getApplicationContext(), "You must fill all fields.",
                    Toast.LENGTH_SHORT).show();

        }

    }

//TODO: I have already an account option



}


