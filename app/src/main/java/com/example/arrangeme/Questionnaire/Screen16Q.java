package com.example.arrangeme.Questionnaire;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * create an instance of this fragment.
 */
public class Screen16Q extends Fragment implements View.OnClickListener {

    RadioGroup rg1;
    RadioGroup rg2;

    public Screen16Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen16_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue16 = view.findViewById(R.id.continue16);
         rg1 = (RadioGroup)getView().findViewById(R.id.radioGroup16);
         rg2 = (RadioGroup)getView().findViewById(R.id.radioGroup161);

        RadioButton rb1 = view.findViewById(R.id.radioButton161);
        RadioButton rb2 = view.findViewById(R.id.radioButton162);
        RadioButton rb3 = view.findViewById(R.id.radioButton163);
        RadioButton rb4 = view.findViewById(R.id.radioButton164);
        RadioButton rb5 = view.findViewById(R.id.radioButton165);
        RadioButton rb6 = view.findViewById(R.id.radioButton166);
        RadioButton rb7 = view.findViewById(R.id.radioButton167);
        RadioButton rb8 = view.findViewById(R.id.radioButton168);
        RadioButton rb9 = view.findViewById(R.id.radioButton169);
        RadioButton rb10 = view.findViewById(R.id.radioButton1610);
        RadioButton[] rb_array = {rb1,rb2,rb3,rb4,rb5,rb6,rb7,rb8,rb9,rb10};
        TextView topMessage = view.findViewById(R.id.text_hello16);
        topMessage.setText("Keep Up The Good Work!");
        continue16.setOnClickListener(this);
        for (RadioButton rb : rb_array)
        {
            rb.setOnClickListener(this);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue16):
                int selectedRadioButtonID1 = rg1.getCheckedRadioButtonId(); //returns -1 if not selected
                int selectedRadioButtonID2 = rg2.getCheckedRadioButtonId(); //returns -1 if not selected

                if (selectedRadioButtonID1 == R.id.radioButton161) {
                    Server.questionnaireFill("20",1);
                } else if (selectedRadioButtonID1 == R.id.radioButton162) {
                    Server.questionnaireFill("20",2);
                } else if (selectedRadioButtonID1 == R.id.radioButton163) {
                    Server.questionnaireFill("20",3);
                } else if (selectedRadioButtonID1 == R.id.radioButton164) {
                    Server.questionnaireFill("20",4);
                } else if (selectedRadioButtonID1 == R.id.radioButton165) {
                    Server.questionnaireFill("20", 5);
                } else if (selectedRadioButtonID2 == R.id.radioButton166) {
                    Server.questionnaireFill("20",6);
                } else if (selectedRadioButtonID2 == R.id.radioButton167) {
                    Server.questionnaireFill("20",7);
                } else if (selectedRadioButtonID2 == R.id.radioButton168) {
                    Server.questionnaireFill("20", 8);
                } else if (selectedRadioButtonID2 == R.id.radioButton169) {
                    Server.questionnaireFill("20",9);
                } else if (selectedRadioButtonID2 == R.id.radioButton1610) {
                    Server.questionnaireFill("20",10);
                }

                navController.navigate(R.id.action_screen16Q_to_screen17Q);
                break;
            default: //radio buttons checked
                rg1.clearCheck();
                rg2.clearCheck();
                ((RadioButton) v).setChecked(true);
                break;
        }
    }



}
