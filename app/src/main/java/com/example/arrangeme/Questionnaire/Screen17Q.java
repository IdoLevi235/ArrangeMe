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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Page number 18 of the questionnaire
 */
public class Screen17Q extends Fragment implements View.OnClickListener {

    private Button yesSportBtn;
    private Button noSportBtn;
    private Button dontKnow2Btn;

    private Button btn_unfocus;

    private Button[] btn = new Button[3];
    private int[] btn_id = {R.id.yesSportBtn, R.id.noSportBtn, R.id.dontKnow2Btn};
    private boolean isReply=false;

    /**
     * empty constructor
     */
    public Screen17Q() {
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
        return inflater.inflate(R.layout.fragment_screen17_q, container, false);
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
        int currentAns = Questionnaire.qarr[20];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }
        Button continue17 = view.findViewById(R.id.continue17);
        continue17.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello17);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentFirebaseUser.getDisplayName();

        topMessage.setText("Nice Going " + username +"!");
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
            case R.id.yesSportBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.noSportBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.dontKnow2Btn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.continue17:
                if (isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("21",1);
                    } else if (btn_unfocus == btn[1]) {
                        Server.questionnaireFill("21",2);
                    } else {
                        Server.questionnaireFill("21",3);
                    }
                }

                navController.navigate(R.id.action_screen17Q_to_screen18Q);
                break;
            default:
                break;
        }
    }

}
