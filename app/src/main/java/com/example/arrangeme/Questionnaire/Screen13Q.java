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
 * Page number 14 of the questionnaire
 */
public class Screen13Q extends Fragment implements View.OnClickListener {

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;


    /**
     * empty constructor
     */
    public Screen13Q() {
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
        return inflater.inflate(R.layout.fragment_screen13_q, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue13 = view.findViewById(R.id.continue13);
        TextView topMessage = view.findViewById(R.id.text_hello13);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton131);
        rb2 = view.findViewById(R.id.radioButton132);
        rb3 = view.findViewById(R.id.radioButton133);
        rb4 = view.findViewById(R.id.radioButton134);
        rb5 = view.findViewById(R.id.radioButton135);

        int currAns = Questionnaire.qarr[16];
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


        continue13.setOnClickListener(this);
    }


    /**
     * @param v
     * on click listener
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue13):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup13);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton131) {
                    Server.questionnaireFill("17",1);
                } else if (selectedRadioButtonID == R.id.radioButton132) {
                    Server.questionnaireFill("17",2);
                } else if (selectedRadioButtonID == R.id.radioButton133) {
                    Server.questionnaireFill("17",3);
                } else if (selectedRadioButtonID == R.id.radioButton134) {
                    Server.questionnaireFill("17",4);
                } else if (selectedRadioButtonID == R.id.radioButton135) {
                    Server.questionnaireFill("17",5);
                }
                navController.navigate(R.id.action_screen13Q_to_screen14Q);
                break;
            default:
                break;
        }
    }

}
