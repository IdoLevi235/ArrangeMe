package com.example.arrangeme.Questionnaire;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Screen2Q extends Fragment implements View.OnClickListener {
    //private FirebaseAuth mAuth;

    private Button personalBtn;
    private Button socialBtn;
    private Button familyBtn;
    private Button CareerBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.personalBtn, R.id.socialBtn, R.id.familyBtn, R.id.careerBtn};
    private Switch smoke;
    private boolean isReply=false;



    public Screen2Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen2_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];
        int currentAns = Questionnaire.qarr[1];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }
        currentAns = Questionnaire.qarr[2];
        smoke=(Switch)getView().findViewById(R.id.isSmoke);
        if(currentAns==2) {
            smoke.setChecked(false);
        }
        else if (currentAns==1){
            smoke.setChecked(true);
        }


        Button continue2 = view.findViewById(R.id.continue2);
        continue2.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello2);
        String username = currentFirebaseUser.getDisplayName();
        topMessage.setText("Thank You " + username + ", Keep Going!");
        isReply=false;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.personalBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.socialBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.familyBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.careerBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[3]);
                break;
            case R.id.continue2:
                if (isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("2",1);
                    } else if (btn_unfocus == btn[1]) {
                        Server.questionnaireFill("2",2);
                    } else if (btn_unfocus==btn[2]) {
                        Server.questionnaireFill("2",3);
                    }else {
                        Server.questionnaireFill("2",4);
                    }
                }
                if (smoke.isChecked())
                    Server.questionnaireFill("3",1);
                else Server.questionnaireFill("3",2);

                navController.navigate(R.id.action_screen2Q_to_screen3Q);
                break;
            default:
                break;
        }
    }
}
