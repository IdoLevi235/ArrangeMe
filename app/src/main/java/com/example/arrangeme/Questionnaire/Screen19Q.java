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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.arrangeme.BuildSchedule.CreateSchedule;
import com.example.arrangeme.Globals;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.R;
import com.example.arrangeme.Server;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Page number 20 of the questionnaire, final page
 */

public class Screen19Q extends Fragment implements View.OnClickListener {

    private Button aloneBtn;
    private Button withMyParentsBtn;
    private Button btn_unfocus;
    private Button[] btn = new Button[2];
    private int[] btn_id = {R.id.aloneBtn, R.id.withMyParentsBtn};
    private boolean isReply = false ;
    private boolean isQFinish=false;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    /**
     * empty constructor
     */
    public Screen19Q() {
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
        return inflater.inflate(R.layout.fragment_screen19_q, container, false);
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
        int currentAns = Questionnaire.qarr[23];
        if(currentAns>0) {
            btn_unfocus = Globals.setFocus(btn_unfocus, btn[currentAns-1]);
        }

        currentAns=Questionnaire.qarr[24];
        rb1 = view.findViewById(R.id.radioButton191);
        rb2 = view.findViewById(R.id.radioButton192);
        rb3 = view.findViewById(R.id.radioButton193);
        if (currentAns==1){
            rb1.setChecked(true);
        }
        else if (currentAns==2){
            rb2.setChecked(true);
        }
        else if (currentAns==3){
            rb3.setChecked(true);
        }

        Button continue19 = view.findViewById(R.id.continue19);
        continue19.setOnClickListener(this);
        TextView topMessage = view.findViewById(R.id.text_hello19);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentFirebaseUser.getDisplayName();
        topMessage.setText("Thank you " + username + ", Just One More To Go!");
        isReply = false;
    }


    /**
     * @param v
     * onclick listsener
     */
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

               isQuestionnaireFilled();
                break;
            default:
                break;
        }
    }

    /**
     * Function that checks at the end of the questionnaire if the user filled all the 25 questions or not
     */
    private void isQuestionnaireFilled() {
        final ArrayList<Integer> q_answers = new ArrayList<Integer>() ;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference currUserRef = mDatabase.child("users").child(Globals.UID).child("personality_vector");
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
                if (!q_answers.contains(0)) {
                    alertQfinish();
                }
                else {
                    alertQnotFinish();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Alert box to the user in case that the questionnaire isn't filled
     */
    private void alertQnotFinish() {
        isQFinish=false;
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog( getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Pay attention!")
                .setContentText(("You haven't finished the questionnaire yet. The system will not provide you " +
                        "schedules recommendations until you will complete the questionnaire. You are now taken to homepage.."));
        ad.setConfirmText("OK");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Intent homepage= new Intent(getActivity(), Homepage.class);
                getActivity().startActivity(homepage);
            }
        });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);


    }
    /**
     * Alert box to the user in case that the questionnaire  filled
     */

    private void alertQfinish() {
        isQFinish=true;
        CreateSchedule ce = new CreateSchedule();
        ce.classifyUserGroup(Globals.UID);
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog( getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Congratulations!")
                .setContentText(("You've completed the questionnaire. Homepage is loading..."));
        ad.setConfirmText("OK");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Intent homepage= new Intent(getActivity(),Homepage.class);
                getActivity().startActivity(homepage);
            }
        });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);

    }


}
