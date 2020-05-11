package com.example.arrangeme.ui.myprofile;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arrangeme.Entities.User;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileInfo extends Fragment implements View.OnClickListener {


    ImageView editModeBtn;
    EditText emailText;
    EditText passwordText;
    EditText firstName;
    EditText lastName;
    TextView email;
    TextView first;
    TextView last;
    TextView password;
    ImageView profileImage;
    private DatabaseReference mDatabase;

    public ProfileInfo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

     return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editModeBtn = (ImageView) view.findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);
        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        first = view.findViewById(R.id.first);
        last = view.findViewById(R.id.last);
        password = view.findViewById(R.id.password);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        emailText.setVisibility(View.GONE);
        passwordText.setVisibility(View.GONE);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        first.setVisibility(View.GONE);
        last.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        checkIfFromGoogle();
    }

    private void checkIfFromGoogle() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("password");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG4", "onDataChange: " + dataSnapshot.getValue());
                if (dataSnapshot.getValue()==null){ //google user
                    Log.d("TAG4", "checkIfFromGoogle: google" + dataSnapshot);
                    Log.d("TAG4", "onDataChange: FROM GOOGLE LOGIN");
                    showDetailsGoogle();
                }
                else { //regular user
                    Log.d("TAG4", "checkIfFromGoogle: regular" + dataSnapshot);
                    Log.d("TAG4", "onDataChange: regular LOGIN");
                    showDetailsRegularUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showDetailsGoogle() {
        Log.d("TAG4", "onDataChange: INSIDE GOOGLE FUNCTION");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailText.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                first.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                disableViews();
                firstName.setX(65);
                emailText.setX(65);
                firstName.setText((String) dataSnapshot.child("fname").getValue());
                emailText.setText((String) dataSnapshot.child("email").getValue());
                editModeBtn.setVisibility(View.GONE);
                Log.d("TAG4", "onDataChange: HELLO");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void showDetailsRegularUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emailText.setVisibility(View.VISIBLE);
                    passwordText.setVisibility(View.VISIBLE);
                    firstName.setVisibility(View.VISIBLE);
                    lastName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    first.setVisibility(View.VISIBLE);
                    last.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    editModeBtn.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    disableViews();
                    emailText.setText((String) dataSnapshot.child("email").getValue());
                    passwordText.setText((String) dataSnapshot.child("password").getValue());
                    firstName.setText((String) dataSnapshot.child("fname").getValue());
                    lastName.setText((String) dataSnapshot.child("lname").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editModeBtn:
                editMode();
                break;
        }
    }

    public void editMode() {

        editModeBtn.setImageResource(R.drawable.ic_check_black_24dp);
        editModeBtn.setBackgroundResource(R.drawable.blue_circle);

        firstName.setEnabled(true);
        firstName.setClickable(true);
        firstName.requestFocus();
        firstName.setCursorVisible(true);

        passwordText.setEnabled(true);
        passwordText.setClickable(true);

        lastName.setEnabled(true);
        lastName.setClickable(true);

        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("Sorry, You can't edit your mail.");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }
        });

        //apply after edit
        editModeBtn.setOnClickListener(new View.OnClickListener(){
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String first = firstName.getText().toString();
            String last = lastName.getText().toString();

            User userToEdit = new User(email,password,first,last);
            //TODO: change the name in globals
            @Override
            public void onClick(View v) {
                //change the details in DB.
                if (passwordText.length() == 0) {
                    SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    ad.setTitleText("Error");
                    ad.setContentText("You must insert a password");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                } else {
                    EditUserInDB(email,password,first, last);
                    SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    ad.setTitleText("Great Job");
                    ad.setContentText("Your profile has been edited");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                }
            }
        });
    }

    public void EditUserInDB(String email, String password, String first, String last) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child("lname").getRef().setValue(last);
                dataSnapshot.child("fname").getRef().setValue(first);
                dataSnapshot.child("password").getRef().setValue(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void disableViews() {
        emailText.setEnabled(false);
        emailText.setClickable(false);

        passwordText.setEnabled(false);
        passwordText.setClickable(false);

        firstName.setEnabled(false);
        firstName.setClickable(false);

        lastName.setEnabled(false);
        lastName.setClickable(false);
    }

    //TODO: when editing keyboard is on top of the text fields.
}
