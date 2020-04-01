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
public class Screen14Q extends Fragment implements View.OnClickListener{


    private Button yesBtn;
    private Button noBtn;
    private Button dontKnowBtn;

    private Button btn_unfocus;

    private Button[] btn = new Button[3];
    private int[] btn_id = {R.id.yesBtn, R.id.noBtn, R.id.dontKnowBtn};


    public Screen14Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen14_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue14 = view.findViewById(R.id.continue14);
        continue14.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello14);
        topMessage.setText("Thank you " + Globals.currentUsername + ", Keep Going!");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.yesBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.noBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.dontKnowBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.continue14:
                navController.navigate(R.id.action_screen14Q_to_screen15Q);
                break;
            default:
                break;
        }
    }

}
