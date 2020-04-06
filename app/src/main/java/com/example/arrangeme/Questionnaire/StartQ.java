package com.example.arrangeme.Questionnaire;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;
import java.util.function.ToDoubleBiFunction;
import static com.example.arrangeme.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartQ<viewPager> extends Fragment  implements View.OnClickListener {

    private Button femaleRec;
    private Button maleRec;
    private Button biSex;
    private Button btn_unfocus;
    private Button[] btn = new Button[3];
    private int[] btn_id = {R.id.femaleRec, R.id.maleRec, R.id.biSex};

    private DatabaseReference mDatabase;


    public StartQ() {
        // Required empty public constructor
    }

    @Override
    //Initializes the layout but not the attributes from the XML
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_start_q, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i = 0; i < btn.length; i++) {
            btn[i] = (Button) getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];

        Button continue1 = view.findViewById(id.continue1);
        continue1.setOnClickListener(this);

        TextView topMessage = view.findViewById(R.id.text_hello1);
        topMessage.setText("Welcome " + Globals.currentUsername );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);
        switch (v.getId()) {
            case id.femaleRec:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[0]);
                break;
            case id.maleRec:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[1]);
                break;
            case id.biSex:
                btn_unfocus=Globals.setFocus(btn_unfocus,btn[2]);
                break;
           case id.continue1:
               //mDatabase = FirebaseDatabase.getInstance().getReference();
               //mDatabase.child("users").child(Globals.UID).child("Personality Vector").child("2").setValue(20);
               navController.navigate(id.action_startQ_to_screen2Q);
            break;
            default:
                break;
        }
    }

//TODO: first pop up too long - need to change and compare with the text in the beginning of this page
}
