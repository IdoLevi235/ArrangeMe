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

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen18Q extends Fragment implements View.OnClickListener {

    private Button aloneGymBtn;
    private Button groupGymBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[2];
    private int[] btn_id = {R.id.aloneGymBtn, R.id.groupGymBtn};
    private boolean isReply = false;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    public Screen18Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen18_q, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];
        int currentAns = Questionnaire.qarr[21];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }

        currentAns=Questionnaire.qarr[22];
        rb1 = view.findViewById(R.id.radioButton181);
        rb2 = view.findViewById(R.id.radioButton182);
        rb3 = view.findViewById(R.id.radioButton183);
        if (currentAns==1){
            rb1.setChecked(true);
        }
        else if (currentAns==2){
            rb2.setChecked(true);
        }
        else if (currentAns==3){
            rb3.setChecked(true);
        }


        Button continue18 = view.findViewById(R.id.continue18);
        continue18.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello18);
        topMessage.setText("You Are Almost Done");
        isReply = false;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.aloneGymBtn:
                isReply = true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.groupGymBtn:
                isReply = true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.continue18:
                if (btn_unfocus == btn[0]) {
                    Server.questionnaireFill("22",1);
                } else {
                    Server.questionnaireFill("22",2);
                }

                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup18);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton181) {
                    Server.questionnaireFill("23",1);
                } else if (selectedRadioButtonID == R.id.radioButton182) {
                    Server.questionnaireFill("23",2);
                } else if (selectedRadioButtonID == R.id.radioButton183) {
                    Server.questionnaireFill("23",3);
                }
                navController.navigate(R.id.action_screen18Q_to_screen19Q);
                break;
            default:
                break;
        }
    }


}
