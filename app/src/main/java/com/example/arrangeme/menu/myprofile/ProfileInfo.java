package com.example.arrangeme.menu.myprofile;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Class that controls profile info fragment
 */
public class ProfileInfo extends Fragment implements View.OnClickListener {

    private boolean hidePass=true;
    ImageView editModeBtn;
    ImageView applyBtn;
    EditText emailText;
    EditText passwordText;
    EditText firstName;
    EditText lastName;
    TextView email;
    TextView first;
    TextView last;
    TextView password;
    ImageView profileImage;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    /**
     * Empty constructor
     */
    public ProfileInfo() {
        // Required empty public constructor
    }


    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editModeBtn = (ImageView) view.findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);
        applyBtn = (ImageView) view.findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);
        emailText = view.findViewById(R.id.emailText);
        emailText.setOnClickListener(this);
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
        passwordText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                Log.d("TAG5", "onTouch: start");
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (passwordText.getRight() - passwordText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        {
                            if (hidePass) {
                                Log.d("TAG5", "onTouch: HIDEPASS=TRUE");
                                passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eyexx,0);
                                hidePass=false;
                            }
                            else if (!hidePass){
                                Log.d("TAG5", "onTouch: HIDEPASS=FALSE");
                                passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eyeee,0);
                                hidePass=true;
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        });

    }

    /**
     * Checking if this is a google user or regular user
     */
    private void checkIfFromGoogle() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info").child("password");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) { //google user
                    showDetailsGoogle();
                } else { //regular user
                    showDetailsRegularUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Show details for google user
     */
    public void showDetailsGoogle() {
        Log.d("TAG4", "onDataChange: INSIDE GOOGLE FUNCTION");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");
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


    /**
     * Disable TetxViews if not in edit mode
     */
    public void disableViews() {
        emailText.setEnabled(false);
        emailText.setClickable(false);

        passwordText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        passwordText.setEnabled(false);
        passwordText.setClickable(false);

        firstName.setEnabled(false);
        firstName.setClickable(false);

        lastName.setEnabled(false);
        lastName.setClickable(false);
    }

    /**
     * Show details for regular user
     */
    public void showDetailsRegularUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");
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

    /**
     * Edit mode
     */
    //transform to edit mode
    public void editMode() {
        editModeBtn.setVisibility(View.GONE);
        applyBtn.setVisibility(View.VISIBLE);
        firstName.setEnabled(true);
        firstName.setClickable(true);
        firstName.requestFocus();
        firstName.setCursorVisible(true);

        passwordText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eyeee,0);
        passwordText.setEnabled(true);
        passwordText.setClickable(true);

        lastName.setEnabled(true);
        lastName.setClickable(true);
    }

    /**
     * After making changes in edit mode this this function write user's new details to DB
     * @param email
     * @param password
     * @param first
     * @param last
     */
    public void EditUserInDB(String email, String password, String first, String last) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
                    }
                });


    }

    /**
     * Apply edit changes
     */
    public void applyMode() {
        if (passwordText.length() == 0) {
            SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            ad.setTitleText("Error");
            ad.setContentText("You must insert a password");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);
        } else {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String first = firstName.getText().toString();
            String last = lastName.getText().toString();
            EditUserInDB(email, password, first, last);
            SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            ad.setTitleText("Great Job");
            ad.setContentText("Your profile has been edited");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);

            applyBtn.setVisibility(View.GONE);
            editModeBtn.setVisibility(View.VISIBLE);
            disableViews();
            //TODO: REFRESH LIKE WE ARE GOING TO ANOTHER TAB AND COME BACK FROM IT

        }
    }


    /**
     * @param v
     */
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.editModeBtn:
                    editMode();
                    break;
                case R.id.applyBtn:
                    applyMode();
                    break;
                case (R.id.emailText):
                    SweetAlertDialog ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    ad.setTitleText("Error");
                    ad.setContentText("Sorry, you can't edit your mail.");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                    break;
            }
        }

}
