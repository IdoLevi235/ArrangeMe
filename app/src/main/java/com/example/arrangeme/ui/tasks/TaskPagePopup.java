package com.example.arrangeme.ui.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.example.arrangeme.R;

public class TaskPagePopup extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.7), (int) (height*0.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);
        //TODO: FADE FROM THE CENTER

    }
}



