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
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue2 = view.findViewById(R.id.continue2);
        continue2.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello2);
        topMessage.setText("Thank you " + Globals.currentUsername + ", Keep Going!");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.personalBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.socialBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.familyBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.careerBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[3]);
                break;
            case R.id.continue2:
                navController.navigate(R.id.action_screen2Q_to_screen3Q);
                break;
            default:
                break;
        }
    }
}
