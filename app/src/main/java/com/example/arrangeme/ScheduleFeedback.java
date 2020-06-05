package com.example.arrangeme;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(date).child("data");

        dislike=findViewById(R.id.dislike);
        dislike.setOnClickListener(this);
        like=findViewById(R.id.like);
        like.setOnClickListener(this);
        doyouthink=findViewById(R.id.doyouthink);

        Bundle b = getIntent().getExtras();
        date = b.getString("date");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("succesfull").equals("n/a")){ // schedule without rate yer
                    doyouthink.setText("According to the suggested schedule for " + date + ", do you fill that you succeed complete most of your tasks? \nWas your day productive?");
                }
                else {

                }
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
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -15;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setAttributes(params);
        this.setFinishOnTouchOutside(false);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.dislike:
                mDatabase.child("succesful").setValue("no");
                break;
            case R.id.like:
                mDatabase.child("succesful").setValue("yes");
                break;
            default:
                break;

        }
    }
}
