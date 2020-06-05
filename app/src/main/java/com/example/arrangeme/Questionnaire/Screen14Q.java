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
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Page number 15 of the questionnaire
 */
public class Screen14Q extends Fragment implements View.OnClickListener{


    private Button yesBtn;
    private Button noBtn;
    private Button dontKnowBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[3];
    private int[] btn_id = {R.id.yesBtn, R.id.noBtn, R.id.dontKnowBtn};
    private boolean isReply=false;


    /**
     * Empty constructor
     */
    public Screen14Q() {
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
        return inflater.inflate(R.layout.fragment_screen14_q, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];
        int currentAns = Questionnaire.qarr[17];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }

        Button continue14 = view.findViewById(R.id.continue14);
        continue14.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello14);
        topMessage.setText("You Got It Right!");
        isReply=false;

    }


    /**
     * @param v
     * onclick listener
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.yesBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.noBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.dontKnowBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.continue14:
                if (isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("18",1);
                    } else if (btn_unfocus == btn[1]) {
                        Server.questionnaireFill("18",2);
                    } else {
                        Server.questionnaireFill("18",3);
                    }
                }
                navController.navigate(R.id.action_screen14Q_to_screen15Q);
                break;
            default:
                break;
        }
    }

}
