package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arrangeme.Questionnaire.Questionnaire;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * This class controls password recovery through mail
 */
public class ForgotPass extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    Button back;
    Button submit;

    /**
     * this function controls what happens on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        email = (EditText) findViewById(R.id.emailText1);
        submit = (Button)findViewById(R.id.submitforgotpass);
        back = (Button)findViewById(R.id.backtologin);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

    }
    /**
     * onClick method, from View.OnClickListener interface
     * including submit button click listener - sends mail with password reset to the user
     * and back to login button
     * @param v
     */

    @Override
    public void onClick(View v) {
        try {
            if (v == submit) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = email.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mailSentSuccess();
                                } else {
                                    ErrorAlert();
                                }
                            }
                        });


            }
        }
        catch(Exception e){
            ErrorAlert();
        }

        if(v==back){
            startActivity(new Intent(ForgotPass.this, Login.class));
        }
    }

    private void mailSentSuccess() {
        SweetAlertDialog ad;
        ad = new SweetAlertDialog(ForgotPass.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Great!")
                .setContentText("Check your E-mail for password reset link.");
        ad.setConfirmText("OK");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);


    }

    private void ErrorAlert() {
        SweetAlertDialog ad;
        ad = new SweetAlertDialog(ForgotPass.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("This email address does not exist in our system");
        ad.setConfirmText("OK");
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);

    }
}
