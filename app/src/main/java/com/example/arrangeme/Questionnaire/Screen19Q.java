package com.example.arrangeme.Questionnaire;

import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.MainActivity;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.example.arrangeme.Signup;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen19Q extends Fragment implements View.OnClickListener {

    private Button aloneBtn;
    private Button withMyParentsBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[2];
    private int[] btn_id = {R.id.aloneBtn, R.id.withMyParentsBtn};
    private boolean isReply = false ;

    public Screen19Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen19_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];
        Button continue19 = view.findViewById(R.id.continue19);
        continue19.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello19);
        topMessage.setText("Thank you " + Globals.currentUsername + ", Just One More To Go!");
        isReply = false;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.aloneBtn:
                isReply = true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.withMyParentsBtn:
                isReply = true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.continue19:
                if (btn_unfocus == btn[0]) {
                    Server.questionnaireFill("24",1);
                } else {
                    Server.questionnaireFill("24",2);
                }

                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup19);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton191) {
                    Server.questionnaireFill("25",1);
                } else if (selectedRadioButtonID == R.id.radioButton192) {
                    Server.questionnaireFill("25",2);
                } else if (selectedRadioButtonID == R.id.radioButton193) {
                    Server.questionnaireFill("25",3);
                }

                Server.isQuestionnaireFilled();
                break;
            default:
                break;
        }
    }


}
