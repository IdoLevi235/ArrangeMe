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
 * Page number 4 of the questionnaire
 */
public class Screen4Q extends Fragment implements View.OnClickListener {
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    RadioButton rb5;


    /**
     * Empty constructor
     */
    public Screen4Q() {
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
        return inflater.inflate(R.layout.fragment_screen4_q, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        Button continue4 = view.findViewById(R.id.continue4);
        TextView topMessage = view.findViewById(R.id.text_hello4);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton41);
        rb2 = view.findViewById(R.id.radioButton42);
        rb3 = view.findViewById(R.id.radioButton43);
        rb4 = view.findViewById(R.id.radioButton44);
        rb5 = view.findViewById(R.id.radioButton45);

        int currAns = Questionnaire.qarr[5];
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
        continue4.setOnClickListener(this);
    }


    /**
     * @param v
     * onclick listeenr
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue4):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton41) {
                    Server.questionnaireFill("6",1);
                }
                else if (selectedRadioButtonID == R.id.radioButton42){
                    Server.questionnaireFill("6",2);
                }else if (selectedRadioButtonID == R.id.radioButton43){
                    Server.questionnaireFill("6",3);
                }else if (selectedRadioButtonID == R.id.radioButton44){
                    Server.questionnaireFill("6",4);
                }else if (selectedRadioButtonID == R.id.radioButton45){
                    Server.questionnaireFill("6",5);
                }
                navController.navigate(R.id.action_screen4Q_to_screen5Q);
                break;
            default:
                break;
        }
    }

}
