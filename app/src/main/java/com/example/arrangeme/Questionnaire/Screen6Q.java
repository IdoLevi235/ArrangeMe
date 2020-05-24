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

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * create an instance of this fragment.
 */
public class Screen6Q extends Fragment implements View.OnClickListener {
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;



    public Screen6Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen6_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue6 = view.findViewById(R.id.continue6);
        TextView topMessage = view.findViewById(R.id.text_hello6);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton61);
        rb2 = view.findViewById(R.id.radioButton62);
        rb3 = view.findViewById(R.id.radioButton63);
        rb4 = view.findViewById(R.id.radioButton64);
        rb5 = view.findViewById(R.id.radioButton65);

        int currAns = Questionnaire.qarr[8];
        if (currAns==1){
            rb1.setChecked(true);
        }
        else if (currAns==2){
            rb2.setChecked(true);
        }
        else if (currAns==3){
            rb3.setChecked(true);
        }
        else if (currAns==4){
            rb4.setChecked(true);
        }
        else if (currAns==5){
            rb5.setChecked(true);
        }


        continue6.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue6):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup6);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton61) {
                    Server.questionnaireFill("9",1);
                } else if (selectedRadioButtonID == R.id.radioButton62) {
                    Server.questionnaireFill("9",1);
                } else if (selectedRadioButtonID == R.id.radioButton63) {
                    Server.questionnaireFill("9",1);
                } else if (selectedRadioButtonID == R.id.radioButton64) {
                    Server.questionnaireFill("9",1);
                } else if (selectedRadioButtonID == R.id.radioButton65) {
                    Server.questionnaireFill("9",1);
                }
                navController.navigate(R.id.action_screen6Q_to_screen7Q);
                break;
            default:
                break;
        }
    }

}
