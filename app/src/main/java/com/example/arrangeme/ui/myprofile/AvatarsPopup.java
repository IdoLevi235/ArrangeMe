package com.example.arrangeme.ui.myprofile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.arrangeme.Homepage;
import com.example.arrangeme.Popup;
import com.example.arrangeme.R;

public class AvatarsPopup extends Activity implements View.OnClickListener, Popup {

    Button AvatarCircle1;
    Button AvatarCircle6;
    Button AvatarCircle3;
    Button AvatarCircle2;
    Button AvatarCircle5;
    Button AvatarCircle4;
    Button apply_avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_choice);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.90 ), (int) (height *0.65));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        apply_avatar = (Button)findViewById(R.id.apply_avatar);
        apply_avatar.setOnClickListener(this);
        this.setFinishOnTouchOutside(false);
        //TODO: FADE FROM THE CENTER

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("FromHomepage", "5");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply_avatar:
                chooseAvatar();
                onBackPressed();
        }
    }

    private void chooseAvatar() {
        return;
    }

    @Override
    public void definePopUpSize() {

    }

    @Override
    public void disableViews() {

    }

    @Override
    public void showDetails() {

    }

    @Override
    public void showImage() {

    }

    @Override
    public void editMode() {

    }
}
