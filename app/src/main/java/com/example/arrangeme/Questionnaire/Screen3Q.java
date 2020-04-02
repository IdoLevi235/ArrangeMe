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


/**
 * A simple {@link Fragment} subclass.
 */
public class Screen3Q extends Fragment implements View.OnClickListener {

    private Button learnAtHome;
    private Button learnAtSchool;

    private Button btn_unfocus;

    private Button[] btn = new Button[2];
    private int[] btn_id = {R.id.learnAtHomeBtn, R.id.learnAtClassBtn};

    public Screen3Q() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen3_q, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue3 = view.findViewById(R.id.continue3);
        continue3.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello3);
        topMessage.setText("You Are Doing Great!");
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case R.id.learnAtHomeBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case R.id.learnAtClassBtn:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case R.id.continue3:
                navController.navigate(R.id.action_screen3Q_to_screen4Q);
                break;
            default:
                break;
        }
    }

}
