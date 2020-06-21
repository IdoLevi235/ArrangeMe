package com.example.arrangeme.menu.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.Homepage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class controls the schedule feedbachs - showing the user a popup and ask if the schedule was succesful
 */
public class ScheduleFeedback extends AppCompatActivity implements View.OnClickListener{
    private Button dislike;
    private Button like;
    private String date;
    private TextView doyouthink;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String UID;
    private DatabaseReference mDatabase;
    private boolean isFromSchedule=false;
    /**
     * this function controls what happens on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_feedback);
        this.definePopUpSize();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        UID=user.getUid();
        dislike=findViewById(R.id.dislike);
        dislike.setOnClickListener(this);
        like=findViewById(R.id.like);
        like.setOnClickListener(this);
        doyouthink=findViewById(R.id.doyouthink);

        Bundle b = getIntent().getExtras();
        date = b.getString("date");
        isFromSchedule = b.getBoolean("isFromScheduleTab");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(date).child("data");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doyouthink.setText("According to the suggested schedule for " + date + ", do you fill that you succeed complete most of your tasks? \nWas your day productive?");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        params.dimAmount = 0.5f;
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -15;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.setFinishOnTouchOutside(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dislike:
                mDatabase.child("successful").setValue("no");
                getPoints();
                Bundle b = new Bundle();
                Intent i1 = new Intent(ScheduleFeedback.this, Homepage.class);
                if (isFromSchedule == true){ // from schedule
                    b.putString("FromHomepage","3");
                    b.putString("date",date);
                    i1.putExtras(b);
                }
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.like:
                mDatabase.child("successful").setValue("yes");
                getPoints();
                Bundle b2 = new Bundle();
                Intent i2 = new Intent(ScheduleFeedback.this, Homepage.class);
                if (isFromSchedule == true){ // from schedule
                    b2.putString("FromHomepage","3");
                    b2.putString("date",date);
                    i2.putExtras(b2);
                }
                startActivity(i2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            default:
                break;

        }
    }

    /**
     * get 5 points and update level after rating a schedule
     */
    private void getPoints() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("personal_info");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long p = (Long) dataSnapshot.child("points").getValue();
                Long newp=p+5;
                dbRef.child("points").setValue(newp);
                if (newp<200)
                    Globals.checkForNewLevel(dbRef,newp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
