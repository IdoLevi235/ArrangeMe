package com.example.arrangeme.ui.myprofile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.arrangeme.R;


public class ProfileInfo extends Fragment implements View.OnClickListener {

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
    public void onClick(View v) {

    }

    //TODO: when editing keyboard is on top of the text fields.
}
