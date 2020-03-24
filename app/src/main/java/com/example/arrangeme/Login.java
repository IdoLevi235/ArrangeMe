package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity implements View.OnClickListener{


    private EditText emailText;
    private EditText passwordText;
    private Button login;
    private ImageView avatar;
    private Button testButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.sumbitBtn);
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==login){
            String password = passwordText.getText().toString();
            String email = emailText.getText().toString();
            signInWithEmailAndPassword(email,password);
        }
    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInWithMail", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Welcome Motek" + user.getDisplayName(),
                                    Toast.LENGTH_SHORT).show();
                            //TODO: New screen after login..
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInWithMail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }
}
//TODO: GOOGLE/FACEBOOK SIGN IN/UP