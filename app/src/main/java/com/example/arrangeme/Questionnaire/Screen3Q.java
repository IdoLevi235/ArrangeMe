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
import android.widget.ImageButton;
import android.widget.RatingBar;
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
 * The third page in the questionnaire
 */
public class Screen3Q extends Fragment implements View.OnClickListener {

    private Button learnAtHome;
    private Button learnAtSchool;
    private Button btn_unfocus;
    private Button[] btn = new Button[2];
    private int[] btn_id = {R.id.learnAtHomeBtn, R.id.learnAtClassBtn};
    private boolean isReply=false;
    private RatingBar ratingBar;
    public Screen3Q() {
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
        return inflater.inflate(R.layout.fragment_screen3_q, container, false);
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
        int currentAns = Questionnaire.qarr[3];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }
        ratingBar = (RatingBar)getView().findViewById(R.id.rateManageTime);
        currentAns=Questionnaire.qarr[4];
        ratingBar.setRating(currentAns);
        Button continue3 = view.findViewById(R.id.continue3);
        continue3.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello3);
        topMessage.setText("You Are Doing Great!");
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
            case R.id.learnAtHomeBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.learnAtClassBtn:
                isReply=true;
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.continue3:
                if(isReply){
                    if (btn_unfocus == btn[0]) {
                        Server.questionnaireFill("4",1);
                    } else  {
                        Server.questionnaireFill("4",2);
                    }
                }
                int rateValue = (int)ratingBar.getRating();
                if (rateValue>0) {
                    Server.questionnaireFill("5",rateValue);
                }
                navController.navigate(R.id.action_screen3Q_to_screen4Q);
                break;
            default:
                break;
        }
    }

}
