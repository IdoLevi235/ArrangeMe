package com.example.arrangeme.ui.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AvatarsPopup extends Activity implements View.OnClickListener{


    Button apply_avatar;
    int ChosenAvatar=0;
    private Button btn_unfocus;
    private Button[] btn = new Button[6];
    HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
    private int[] btn_id = {R.id.AvatarCircle1, R.id.AvatarCircle6, R.id.AvatarCircle3, R.id.AvatarCircle2, R.id.AvatarCircle5, R.id.AvatarCircle4};
    private int[] drawable = {R.drawable.circle_avatar1, R.drawable.circle_avatar6, R.drawable.circle_avatar3, R.drawable.circle_avatar2, R.drawable.circle_avatar5, R.drawable.circle_avatar4};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_choice);

        apply_avatar = (Button)findViewById(R.id.apply_avatar);
        apply_avatar.setOnClickListener(this);

        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button)findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
            hash.put(btn_id[i],drawable[i]);
        }

        btn_unfocus = btn[0];

        this.setFinishOnTouchOutside(false);
        definePopUpSize();

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
                updateAvatar(ChosenAvatar);
                break;
            case R.id.AvatarCircle1:
                ChosenAvatar=1;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[0],hash.get(btn_unfocus.getId()));
                break;
            case R.id.AvatarCircle6:
                ChosenAvatar=6;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[1],hash.get(btn_unfocus.getId()));
                break;
            case R.id.AvatarCircle3:
                ChosenAvatar=3;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[2], hash.get(btn_unfocus.getId()));
                break;
            case R.id.AvatarCircle2:
                ChosenAvatar=2;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[3],hash.get(btn_unfocus.getId()));
                break;
            case R.id.AvatarCircle5:
                ChosenAvatar=5;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[4], hash.get(btn_unfocus.getId()));
                break;
            case R.id.AvatarCircle4:
                ChosenAvatar=4;
                btn_unfocus=Globals.setFocusAvatars(btn_unfocus,btn[5],hash.get(btn_unfocus.getId()));
                break;
        }
    }

    private void updateAvatar(int ChosenAvatar) {
        String avatar= "avatar"+ChosenAvatar;
        Log.d("avatar", "updateAvatar: avatar"+avatar);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");
        mDatabase.child("avatar").setValue(avatar);

        onBackPressed();
        return;
    }


    public void definePopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.90 ), (int) (height *0.65));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount = 0.5f;
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
