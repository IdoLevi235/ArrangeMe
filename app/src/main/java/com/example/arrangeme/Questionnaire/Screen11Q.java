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
public class Screen11Q extends Fragment implements View.OnClickListener {
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;



    public Screen11Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen11_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue11 = view.findViewById(R.id.continue11);
        TextView topMessage = view.findViewById(R.id.text_hello11);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton111);
        rb2 = view.findViewById(R.id.radioButton112);
        rb3 = view.findViewById(R.id.radioButton113);
        rb4 = view.findViewById(R.id.radioButton114);
        rb5 = view.findViewById(R.id.radioButton115);

        int currAns = Questionnaire.qarr[14];
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

        continue11.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue11):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup11);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton111) {
                    Server.questionnaireFill("15",1);
                } else if (selectedRadioButtonID == R.id.radioButton112) {
                    Server.questionnaireFill("15",2);
                } else if (selectedRadioButtonID == R.id.radioButton113) {
                    Server.questionnaireFill("15",3);
                } else if (selectedRadioButtonID == R.id.radioButton114) {
                    Server.questionnaireFill("15",4);
                } else if (selectedRadioButtonID == R.id.radioButton115) {
                    Server.questionnaireFill("15",5);
                }
                navController.navigate(R.id.action_screen11Q_to_screen12Q);
                break;
            default:
                break;
        }
    }

}
