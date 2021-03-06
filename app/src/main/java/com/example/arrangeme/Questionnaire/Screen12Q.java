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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Page number 13 of the questionnaire
 */
public class Screen12Q extends Fragment implements View.OnClickListener {

    private Button yesBtn;
    private Button eightBtn;
    private Button tenBtn;
    private Button twelveBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.sixBtn, R.id.eightBtn, R.id.tenBtn, R.id.twelveBtn};
    private boolean isReply=false;

    /**
     * empty constructor
     */
    public Screen12Q() {
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
        return inflater.inflate(R.layout.fragment_screen12_q, container, false);
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
        int currentAns = Questionnaire.qarr[15];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }

        Button continue12 = view.findViewById(R.id.continue12);
        continue12.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello12);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentFirebaseUser.getDisplayName();
        topMessage.setText("Great Job " + username +"!");
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
            case R.id.sixBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.eightBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.tenBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.twelveBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[3]);
                break;
            case R.id.continue12:
                if (isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("16",1);
                    } else if (btn_unfocus == btn[1]) {
                        Server.questionnaireFill("16",2);
                    } else if (btn_unfocus==btn[2]) {
                        Server.questionnaireFill("16",3);
                    }else {
                        Server.questionnaireFill("16",4);
                    }
                }

                navController.navigate(R.id.action_screen12Q_to_screen13Q);
                break;
            default:
                break;
        }
    }

}
