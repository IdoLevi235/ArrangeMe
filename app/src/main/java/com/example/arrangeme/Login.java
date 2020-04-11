package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText emailText;
    private EditText passwordText;
    private Button login;
    private ImageView avatar;
    private Button testButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button signUpWithGoogleBtn;
    private Button getSignUpWithFacebookBtn;
    private TextView forgotPass;

    //private Button getSignUpWithFacebookBtn;

    private SignInButton sign_in_google; //google try
    private Button sign_out_button;
    private static final int RC_SIGN_IN =1;
    private static final String TAG = "SignInActivity";
    private GoogleSignInClient mGoogleSignInClient;

    private TextView mStatusTextView;

    @Override
    protected void onStart() {
        Log.d("onStart", "onStart Happened");
        super.onStart();
       //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       //updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.toolbar);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        login = (Button) findViewById(R.id.sumbitBtn);
        forgotPass = (TextView)findViewById(R.id.forgetPass);
        login.setOnClickListener(this);
        forgotPass.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();//?


        sign_in_google = findViewById(R.id.sign_in_button);
        sign_out_button = findViewById(R.id.sign_out_button);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_out_button).setOnClickListener(this);
        mStatusTextView = findViewById(R.id.status);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sumbitBtn:
                Log.d("login", "login Btn Activated");
                String password = passwordText.getText().toString();
                String email = emailText.getText().toString();
                signInWithEmailAndPassword(email, password);
                break;
            case R.id.sign_in_button:
                Log.d("google", "google Btn Activated");
                signInWithGoogle();
                break;
           case R.id.sign_out_button:
                Log.d("google", "google Btn Activated");
                signOutWithGoogle();
                break;
            case R.id.forgetPass:
                startActivity(new Intent(Login.this, ForgotPass.class));
            default:
                break;
        }

    }

    private void signOutWithGoogle() {
        mGoogleSignInClient.signOut();
        Toast.makeText(Login.this,"You Are Logged Out", Toast.LENGTH_SHORT).show();
        sign_out_button.setVisibility(View.INVISIBLE);
    }

    //google sign-in method
    private void signInWithGoogle() {
        Log.d("signInWithGoogle", "signInWithGoogle Happened");
        FirebaseUser user = mAuth.getCurrentUser();


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutFromGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        FirebaseGoogleAuth(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    //google sign-in returned value
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "onActivityResult Happened");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            Log.d("handleSignInResult", "handleSignInResult Happened");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            Log.d("ApiException", "ApiException Happened");
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {


            AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                    Toast.makeText(Login.this,"Success!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    }
                    else{
                        Toast.makeText(Login.this,"Failed!", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }


    private void updateUI(FirebaseUser fUser){
        sign_out_button.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            Globals.currentUsername=account.getGivenName(); //update user's name
            Globals.UID = fUser.getUid();
            Globals.currentEmail = account.getEmail();


            /* addNewUserToDB */
            mDatabase = FirebaseDatabase.getInstance().getReference();
            User userToAdd = new User(Globals.currentEmail,Globals.currentUsername);
            mDatabase.child("users").child(Globals.UID).setValue(userToAdd);
            /* addNewUserToDB end */


            //String user_email = account.getEmail(); //update user's mail
            //emailText.setText(user_email); // put his email on the screen
            //Toast.makeText(Login.this,user_email, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, Homepage.class));
            //Uri personPhoto = account.getPhotoUrl(); //TODO: photo if we would like
        }



        //TODO: update it's mail on the DB + check how the password updates?

    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth = FirebaseAuth.getInstance(); //Firebase Authentication instanc
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignInWithMail", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Welcome  " + user.getDisplayName(),
                                        Toast.LENGTH_SHORT).show();
                                //TODO: New screen after login..
                                Globals.currentUsername = user.getDisplayName();
                                Globals.currentEmail = user.getEmail();
                                Globals.UID = user.getUid();
                                startActivity(new Intent(Login.this, Homepage.class));
                            } else {
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e) {
                                    createAlert(e.getMessage(), SweetAlertDialog.ERROR_TYPE);
                                }
                            } //end else
                        }
                    });
        }//end of try


        catch (IllegalArgumentException e) {
            createAlert("You must fill all fields.", SweetAlertDialog.ERROR_TYPE);
        }


    }

    private void createAlert(String msg, int type) {
        SweetAlertDialog ad = new SweetAlertDialog(Login.this, type);
        ad.setTitleText("Error");
        ad.setContentText(msg);
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);

    }
}




//TODO: remember me option
//TODO: FACEBOOK SIGN IN/UP
