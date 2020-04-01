package com.example.arrangeme;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import androidx.annotation.RequiresApi;

public class Globals {
    private  Button btn_unfocus;
    private  Button btn_focus;
    public static String currentUsername;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")


    //This function acting like a "radio group" like with buttons
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

}
