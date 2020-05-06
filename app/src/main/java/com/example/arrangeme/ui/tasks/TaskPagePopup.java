package com.example.arrangeme.ui.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TaskPagePopup extends Activity  implements View.OnClickListener{

    private ImageView applyBtn;
    private ImageView editModeBtn;
    private TextView  descriptionText;
    private TextView  locationText;
    private TextView textCategory;
    private Uri image;
    private DatabaseReference mDatabase;
    private ReminderType chosenReminder;
    private TaskEntity task;
    private String taskKey;
    private TaskEntity taskToPresent;
    private TaskCategory taskCategory;
    private ReminderType reminderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskpagepopup);
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
        Bundle b = getIntent().getExtras();
        if(b != null)
            taskKey = b.getString("TaskKey");

        //TODO: FADE FROM THE CENTER

        applyBtn=findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);

        editModeBtn=findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);

        descriptionText =findViewById(R.id.descriptionText);
        locationText =findViewById(R.id.locationText);
        textCategory= findViewById(R.id.textCategory);

        showTaskDetails();
    }

    //shows the task details
    private void showTaskDetails() {
        showImage();
        TaskEntity taskToPresent =new TaskEntity();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //description
                taskToPresent.setDescription((String) dataSnapshot.child(taskKey).child("description").getValue());
                Log.d("TAG", "onDataChange: "+taskToPresent.getDescription());
                //category
                String category = (String) dataSnapshot.child(taskKey).child("category").getValue();
                int x= taskCategory.fromStringToInt(category);
                taskToPresent.setCategory(taskCategory);
                //location
                taskToPresent.setLocation((String) dataSnapshot.child(taskKey).child("location").getValue());
                locationText.setText(taskToPresent.getLocation());
                descriptionText.setText(taskToPresent.getDescription());
                Integer[] catIcon = {R.drawable.study, R.drawable.sport,  R.drawable.work, R.drawable.nutrition, R.drawable.familycat, R.drawable.chores, R.drawable.relax, R.drawable.friends_cat, 0};
                Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};
                textCategory.setText(category);
                textCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), catColor[x]));
                textCategory.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,catIcon[x],0);
                textCategory.setPadding(0,0,90,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
        //shows the task's details in the activity layout


    private void showImage(){
        //TODO: shows the image from DB - add the photo to the DB
   }
    //return o task tab
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("FromHomepage", "1");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //turn on the edit mode
    private void editMode() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.applyBtn):
                onBackPressed();
                break;
            case (R.id.editModeBtn):
                editMode();
                break;
            default:
                onBackPressed();
                break;
        }
    }

}
