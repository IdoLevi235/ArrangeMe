package com.example.arrangeme.Questionnaire;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen16Q extends Fragment implements View.OnClickListener{
    private int count = 0 ;
    private int last;

    public Screen16Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_screen16_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView topMessage = view.findViewById(R.id.text_hello16);
        topMessage.setText("Thank you " + Globals.currentUsername + ", Keep Going!");
        Button continue16 = view.findViewById(R.id.continue16);
        continue16.setOnClickListener(this);
        CheckBox c1 = (CheckBox) getView().findViewById(R.id.checkBoxfacebook);
        CheckBox c2 = (CheckBox) getView().findViewById(R.id.checkBoxwhatsapp);
        CheckBox c3 = (CheckBox) getView().findViewById(R.id.checkBoxtwitter);
        CheckBox c4 = (CheckBox) getView().findViewById(R.id.checkBoxyoutube);
        CheckBox c5 = (CheckBox) getView().findViewById(R.id.checkBoxspotify);
        CheckBox c6 = (CheckBox) getView().findViewById(R.id.checkBoxinsetgram);
        CheckBox c7 = (CheckBox) getView().findViewById(R.id.checkBoxtiktok);
        CheckBox c8 = (CheckBox) getView().findViewById(R.id.checkBoxwaze);
        CheckBox c9 = (CheckBox) getView().findViewById(R.id.checkBoxnews);
        CheckBox c10 = (CheckBox) getView().findViewById(R.id.checkBoxsports);
        CheckBox c11 = (CheckBox) getView().findViewById(R.id.checkBoxother);
        CheckBox[] values = {c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11};
        for(CheckBox c : values) {
            c.setOnClickListener(this);
        }
    }

    @Override
    @SuppressLint("ResourceAsColor")
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        if (v.getId() == R.id.continue16) { //continue
            navController.navigate(R.id.action_screen16Q_to_screen17Q);
        } else {//checkboxes
            CheckBox c = ((CheckBox) v);
            if(c.isChecked()) { //check
                if (count < 2) {
                    count++;
                } else if (count == 2) { //more than 2, uncheck  the one that is not "last"
                    Toast.makeText(getActivity(), "You can choose only 2 apps!", Toast.LENGTH_SHORT).show();
                    CheckBox cc = (CheckBox) getView().findViewById(v.getId());
                    cc.setChecked(false);
                }
            }

            else { //uncheck
                count--;
            }

            }
        }
    }

