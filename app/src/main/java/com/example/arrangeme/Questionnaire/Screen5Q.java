package com.example.arrangeme.Questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arrangeme.R;


/**
 * create an instance of this fragment.
 */
public class Screen5Q extends Fragment {

    public Screen5Q() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen5_q, container, false);
    }
}
