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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * create an instance of this fragment.
 */
public class Screen13Q extends Fragment implements View.OnClickListener {


    public Screen13Q() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen13_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Button continue13 = view.findViewById(R.id.continue13);
        TextView topMessage = view.findViewById(R.id.text_hello13);
        topMessage.setText("Keep Up The Good Work!");
        continue13.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case (R.id.continue13):
                RadioGroup rg = (RadioGroup)getView().findViewById(R.id.radioGroup13);
                int selectedRadioButtonID = rg.getCheckedRadioButtonId(); //returns -1 if not selected
                if (selectedRadioButtonID == R.id.radioButton131) {
                    Server.questionnaireFill("17",1);
                } else if (selectedRadioButtonID == R.id.radioButton132) {
                    Server.questionnaireFill("17",2);
                } else if (selectedRadioButtonID == R.id.radioButton133) {
                    Server.questionnaireFill("17",3);
                } else if (selectedRadioButtonID == R.id.radioButton134) {
                    Server.questionnaireFill("17",4);
                } else if (selectedRadioButtonID == R.id.radioButton135) {
                    Server.questionnaireFill("17",5);
                }
                navController.navigate(R.id.action_screen13Q_to_screen14Q);
                break;
            default:
                break;
        }
    }

}
