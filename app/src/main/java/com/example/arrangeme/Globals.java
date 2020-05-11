package com.example.arrangeme;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Globals {
    public static String UID;
    public static String currentEmail;
    private  Button btn_unfocus2;
    private  Button btn_focus;
    private  Button btn_unfocus2a;
    private  Button btn_unfocus2b;
    private  Button btn_focus2;
    public static String currentUsername = "test";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")


    //This function acting like a "radio group" like with buttons with one parameter for the Button unfocused
    //use: in the fragments of the questionnaire
    public static Button setFocus(Button btn_unfocus, Button btn_focus){
        //define the colors of the button that is unfocused
        btn_unfocus.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        btn_unfocus.setTextColor(Color.parseColor("#000000"));
        btn_unfocus.setBackgroundResource(R.drawable.rounded_rec_white);
        //define the colors of the button that is focused
        btn_focus.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        btn_focus.setTextColor(Color.parseColor("#FFFFFF"));
        btn_focus.setBackgroundResource(R.drawable.rounded_rec_blue);

        //return the button that is focused
        return btn_focus;
    }

    //This function acting like a "radio group" like with buttons with two parameter for the Button unfocused
    //use: in the Calendar main screen tab
public static Button setFocus2(Button btn_unfocus2a, Button btn_unfocus2b, Button btn_focus2){
        //define the colors of the button that is unfocused
        btn_unfocus2a.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#75C1C9")));
        btn_unfocus2a.setTextColor(Color.parseColor("#333333"));
        btn_unfocus2a.setBackgroundResource(R.drawable.rounded_rec_blueborder);

        btn_unfocus2b.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#75C1C9")));
        btn_unfocus2b.setTextColor(Color.parseColor("#333333"));
        btn_unfocus2b.setBackgroundResource(R.drawable.rounded_rec_blueborder);

        //define the colors of the button that is focused
        btn_focus2.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        btn_focus2.setTextColor(Color.parseColor("#FFFFFF"));
        btn_focus2.setBackgroundResource(R.drawable.rounded_rec_blue_nostroke);

        //return the button that is focused
        return btn_focus2;
    }


    //This function acting like a "radio group" like with buttons with one parameter for the Button unfocused
    //use: in the fragments of the questionnaire
    public static Button setFocusAvatars(Button btn_unfocus, Button btn_focus, int drawable){

        //define the colors of the button that is unfocused

        btn_unfocus.setBackgroundResource(drawable);

        //define the colors of the button that is focused
        btn_focus.setBackgroundResource(R.drawable.blue_circle);

        //return the button that is focused
        return btn_focus;
    }



}
