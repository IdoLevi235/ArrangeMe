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
 * Page number 11 of the questionnaire
 */
public class Screen10Q extends Fragment implements View.OnClickListener {

    private Button morningBtn;
    private Button eveningBtn;
    private Button noonBtn;
    private Button nightBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.morningBtn, R.id.eveningBtn, R.id.noonBtn, R.id.nightBtn};
    private boolean isReply=false;


    /**
     * empty constructor
     */
    public Screen10Q() {
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
        return inflater.inflate(R.layout.fragment_screen10_q, container, false);
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
        int currAns = Questionnaire.qarr[13];
        if(currAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currAns-1]);
        }

        Button continue10 = view.findViewById(R.id.continue10);
        continue10.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello10);
        topMessage.setText("You Are Halfway There!");
        isReply=false;
    }


    /**
     * @param v
     * on click listener
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.morningBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.eveningBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.noonBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.nightBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[3]);
                break;
            case R.id.continue10:
                if (isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("14",1);
                    } else if (btn_unfocus == btn[1]) {
                        Server.questionnaireFill("14",2);
                    } else if (btn_unfocus==btn[2]) {
                        Server.questionnaireFill("14",3);
                    }else {
                        Server.questionnaireFill("14",4);
                    }
                }
                navController.navigate(R.id.action_screen10Q_to_screen11Q);
                break;
            default:
                break;
        }
    }

}
