package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

/**
 * This class controls the schedule feedbachs - showing the user a popup and ask if the schedule was succesful
 */
public class ScheduleFeedback extends AppCompatActivity implements View.OnClickListener{
    private Button dislike;
    private Button like;

    /**
     * this function controls what happens on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_feedback);
        this.definePopUpSize();
        dislike=findViewById(R.id.dislike);
        dislike.setOnClickListener(this);
        like=findViewById(R.id.like);
        like.setOnClickListener(this);
    }

    /**
     * Define popup size
     */
    private void definePopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width *0.9 ), (int) (height *0.78));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -15;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setAttributes(params);
        this.setFinishOnTouchOutside(false);
    }
    @Override
    public void onClick(View v) {

    }
}
