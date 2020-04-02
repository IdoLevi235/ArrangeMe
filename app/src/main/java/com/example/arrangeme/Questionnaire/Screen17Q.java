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
public class Screen17Q extends Fragment implements View.OnClickListener {

    private Button yesSportBtn;
    private Button noSportBtn;
    private Button dontKnow2Btn;

    private Button btn_unfocus;

    private Button[] btn = new Button[3];
    private int[] btn_id = {R.id.yesSportBtn, R.id.noSportBtn, R.id.dontKnow2Btn};

    public Screen17Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen17_q, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue17 = view.findViewById(R.id.continue17);
        continue17.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello17);
        topMessage.setText("Nice Going " + Globals.currentUsername +"!");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.yesSportBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.noSportBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.dontKnow2Btn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
            case R.id.continue17:
                navController.navigate(R.id.action_screen17Q_to_screen18Q);
                break;
            default:
                break;
        }
    }

}
