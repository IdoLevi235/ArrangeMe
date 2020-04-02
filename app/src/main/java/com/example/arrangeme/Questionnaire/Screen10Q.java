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

/**
 * A simple {@link Fragment} subclass.
 */
public class Screen10Q extends Fragment implements View.OnClickListener {

    private Button morningBtn;
    private Button eveningBtn;
    private Button noonBtn;
    private Button nightBtn;

    private Button btn_unfocus;

    private Button[] btn = new Button[4];
    private int[] btn_id = {R.id.morningBtn, R.id.eveningBtn, R.id.noonBtn, R.id.nightBtn};


    public Screen10Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen10_q, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue10 = view.findViewById(R.id.continue10);
        continue10.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello10);
        topMessage.setText("You Are Halfway There!");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.morningBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.eveningBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.noonBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.nightBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[3]);
                break;
            case R.id.continue10:
                navController.navigate(R.id.action_screen10Q_to_screen11Q);
                break;
            default:
                break;
        }
    }

}
