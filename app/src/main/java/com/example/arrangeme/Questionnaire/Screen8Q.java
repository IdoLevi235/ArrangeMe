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
 * Page number 9 of the questionnaire
 */
public class Screen8Q extends Fragment implements View.OnClickListener {
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;


    /**
     * Empty constructor
     */
    public Screen8Q() {
        // Required empty public constructor
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen8_q, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue8 = view.findViewById(R.id.continue8);
        TextView topMessage = view.findViewById(R.id.text_hello8);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton81);
        rb2 = view.findViewById(R.id.radioButton82);
        rb3 = view.findViewById(R.id.radioButton83);
        rb4 = view.findViewById(R.id.radioButton84);

        int currAns = Questionnaire.qarr[11];
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

        continue8.setOnClickListener(this);
    }


    /**
     * @param v
     * onclick listener
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue8):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup8);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton81) {
                    Server.questionnaireFill("12",1);
                } else if (selectedRadioButtonID == R.id.radioButton82) {
                    Server.questionnaireFill("12",2);
                } else if (selectedRadioButtonID == R.id.radioButton83) {
                    Server.questionnaireFill("12",3);
                } else if (selectedRadioButtonID == R.id.radioButton84) {
                    Server.questionnaireFill("12",4);
                }
                navController.navigate(R.id.action_screen8Q_to_screen9Q);
                break;
            default:
                break;
        }
    }

}
