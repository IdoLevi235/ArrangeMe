package com.example.arrangeme.ui.myprofile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.TaskPagePopup;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileInfo extends Fragment implements View.OnClickListener {


    ImageView editModeBtn;
    EditText emailText;
    EditText passwordText;
    EditText firstName;
    EditText lastName;
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
        editModeBtn = (ImageView) view.findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);

        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);

        disableViews();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editModeBtn:
                editMode();
                break;

        }
    }

    private void editMode() {

        editModeBtn.setImageResource(R.drawable.ic_check_black_24dp);
        editModeBtn.setBackgroundResource(R.drawable.blue_circle);

        passwordText.setEnabled(true);
        passwordText.setClickable(true);
        passwordText.requestFocus();
        passwordText.setCursorVisible(true);

        firstName.setEnabled(true);
        firstName.setClickable(true);

        lastName.setEnabled(true);
        lastName.setClickable(true);

        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog ad = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("Sorry, You can't edit your mail.");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }
        });

        editModeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //change the details in DB.

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
