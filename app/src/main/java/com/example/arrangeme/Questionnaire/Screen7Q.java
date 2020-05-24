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
import android.widget.Switch;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * create an instance of this fragment.
 */
public class Screen7Q extends Fragment implements View.OnClickListener {

    private Switch isMed;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;

    public Screen7Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen7_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        Button continue7 = view.findViewById(R.id.continue7);
        isMed = (Switch)getView().findViewById(R.id.isMed);

        TextView topMessage = view.findViewById(R.id.text_hello7);
        topMessage.setText("Keep Up The Good Work!");
        int currAns = Questionnaire.qarr[9];
        if (currAns==2){
            isMed.setChecked(false);
        }
        else if (currAns==1){
            isMed.setChecked(true);
        }

        rb1 = view.findViewById(R.id.radioButton71);
        rb2 = view.findViewById(R.id.radioButton72);
        rb3 = view.findViewById(R.id.radioButton73);
        rb4 = view.findViewById(R.id.radioButton74);
        rb5 = view.findViewById(R.id.radioButton75);

        currAns = Questionnaire.qarr[10];
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

        continue7.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {

            case (R.id.continue7):

                if (isMed.isChecked())
                    Server.questionnaireFill("10",1);
                else Server.questionnaireFill("10",2);



                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup7);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton71) {
                    Server.questionnaireFill("11",1);
                } else if (selectedRadioButtonID == R.id.radioButton72) {
                    Server.questionnaireFill("11",2);
                } else if (selectedRadioButtonID == R.id.radioButton73) {
                    Server.questionnaireFill("11",3);
                } else if (selectedRadioButtonID == R.id.radioButton74) {
                    Server.questionnaireFill("11",4);
                } else if (selectedRadioButtonID == R.id.radioButton75) {
                    Server.questionnaireFill("11",5);
                }
                navController.navigate(R.id.action_screen7Q_to_screen8Q);
                break;
            default:
                break;
        }
    }

}
