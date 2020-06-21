package com.example.arrangeme;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Global functions and varaibles
 */
public class Globals {
    /**
     * Current user's UID
     */
    public static String UID;
    /**
     * Current user's Email
     */
    public static String currentEmail;
    public final static int MAX_POINTS = 200;
    public final static int LEVELS_AMOUNT = 10;
    private  Button btn_unfocus2;
    private  Button btn_focus;
    private  Button btn_unfocus2a;
    private  Button btn_unfocus2b;
    private  Button btn_focus2;
    /**
     * Current username
     */
    public static String currentUsername;
    public static boolean isNewUser;


    /**
     *     This function acting like a "radio group" like with buttons with one parameter for the Button unfocused
     *     use: in the fragments of the questionnaire
     * @param btn_unfocus
     * @param btn_focus
     * @return Button - the button to focus on
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")


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

    /**
     *     This function acting like a "radio group" like with buttons with two parameter for the Button unfocused
     *     use: in the Calendar main screen tab
     * @param btn_unfocus2a
     * @param btn_unfocus2b
     * @param btn_focus2
     * @return Button - the button to focus on
     */
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


    /**
     *     This function acting like a "radio group" like with buttons with one parameter for the Button unfocused
     *     use: in the fragments of the questionnaire
     * @param btn_unfocus
     * @param btn_focus
     * @param hash
     * @return Button - the button to focus on
     */
    public static Button setFocusAvatars(Button btn_unfocus, Button btn_focus, Integer hash){

        //define the colors of the button that is unfocused

       btn_unfocus.setBackgroundResource(hash);
        //btn_unfocus.setBackgroundResource(hash.get(btn_unfocus));

        //define the colors of the button that is focused

        btn_focus.setBackgroundResource(R.drawable.avatar_choose);
        //return the button that is focused
        return btn_focus;
    }


    public void simulateUsers() {
    }

    public static void checkForNewLevel(DatabaseReference dbRef, Long points){
        ArrayList<String> levels = new ArrayList<>();
        levels.add("Beginner"); levels.add("Intermediate");
        levels.add("Tasks Skilled"); levels.add("Advanced");
        levels.add("Experienced Scheduler");levels.add("Professional");
        levels.add("Specialist");levels.add("Master of Schedules");
        levels.add("Grand Master of Schedules"); levels.add("Ultimate Master of Schedules");
        int sum = MAX_POINTS;
        int count=LEVELS_AMOUNT;
        if (points<MAX_POINTS){
            while (sum>points){
                sum-=20;
                count--;
            }
            String level  = levels.get(count);
            dbRef.child("level").setValue(level);
        }
    }
}
