package com.example.arrangeme.Questionnaire;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.arrangeme.R;
import org.w3c.dom.Text;
import java.util.function.ToDoubleBiFunction;
import static com.example.arrangeme.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartQ extends Fragment  implements View.OnClickListener {
    private Button femaleRec;
    private ImageView female;
    private Button maleRec;
    private ImageView male;
    private Button biSex;

    private Button[] btn = new Button[3];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.femaleRec, R.id.maleRec, R.id.biSex};

    public StartQ() {
        // Required empty public constructor
    }

    @Override
    //Initializes the layout but not the attributes from the XML
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_start_q, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)getView().findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }
        btn_unfocus = btn[0];
        Button continue1 = view.findViewById(id.continue1);
        continue1.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    public void onClick(View v){ //check for what button is pressed
        final NavController navController = Navigation.findNavController(v);

        //btn_unfocus.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        switch (v.getId()) {
            case id.femaleRec:
                setFocus(btn_unfocus,btn[0]);
                //female.setColorFilter(getContext().getResources().getColor(color.colorWhite));
                break;
            case id.maleRec:
                setFocus(btn_unfocus, btn[1]);
                //male.setColorFilter(getContext().getResources().getColor(color.colorWhite));
                break;
            case id.biSex:
                setFocus(btn_unfocus, btn[2]);
                break;
           case id.continue1:
                navController.navigate(id.action_startQ_to_screen2Q);
            break;
            default:
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.parseColor("#000000"));

        btn_unfocus.setBackgroundResource(R.drawable.rounded_rec_white);
        btn_focus.setTextColor(Color.parseColor("#FFFFFF"));
        btn_focus.setBackgroundResource(drawable.rounded_rec_blue);
        this.btn_unfocus = btn_focus;
    }

}
