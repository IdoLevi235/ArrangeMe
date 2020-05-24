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
public class Screen61Q extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;


    public Screen61Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen61_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        Button continue61 = view.findViewById(R.id.continue61);
        TextView topMessage = view.findViewById(R.id.text_hello61);
        topMessage.setText("Keep Up The Good Work!");
        rb1 = view.findViewById(R.id.radioButton611);
        rb2 = view.findViewById(R.id.radioButton612);
        rb3 = view.findViewById(R.id.radioButton613);
        rb4 = view.findViewById(R.id.radioButton614);
        rb5 = view.findViewById(R.id.radioButton615);

        int currAns = Questionnaire.qarr[7];
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


        continue61.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue61):
                mDatabase = FirebaseDatabase.getInstance().getReference();
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup61);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton611) {
                    Server.questionnaireFill("8",1);
                } else if (selectedRadioButtonID == R.id.radioButton612) {
                    Server.questionnaireFill("8",2);
                } else if (selectedRadioButtonID == R.id.radioButton613) {
                    Server.questionnaireFill("8",3);
                } else if (selectedRadioButtonID == R.id.radioButton614) {
                    Server.questionnaireFill("8",4);
                } else if (selectedRadioButtonID == R.id.radioButton615) {
                    Server.questionnaireFill("8",5);
                }
                navController.navigate(R.id.action_screen61Q_to_screen6Q);
                break;
            default:
                break;
        }
    }

}
