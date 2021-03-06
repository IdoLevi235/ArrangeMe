package com.example.arrangeme.menu.myprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.ReminderBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Settings activity
 */
public class Settings extends AppCompatActivity implements View.OnClickListener{
    DatabaseReference mDatabase;
    Button applyBtn;
    String currUID;
    Switch appNotif;
    Switch phoneNotif;
    Switch buildYour;
    Switch googleSync;
    Switch quesSwitch;
    Button backBtn;
    Button ques;
    ArrayList<Integer> q_answers = new ArrayList<>();
    int answers[] = new int[25];

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        currUID = currentFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currUID).child("settings");
        showPrevValues();

        Log.d("TAG6", "onCreate: " + currUID);
        applyBtn = findViewById(R.id.applyBtn);
        backBtn = findViewById(R.id.backBtn);
        applyBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        appNotif = findViewById(R.id.App);
        phoneNotif=findViewById(R.id.Phone);
        buildYour=findViewById(R.id.build);
        googleSync=findViewById(R.id.google);
        ques=findViewById(R.id.fillTheQuestion);
        ques.setOnClickListener(this);
        quesSwitch=findViewById(R.id.ques_switch);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currUID).child("personality_vector");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
                int i = 0;

                for (Integer x : q_answers) {
                    answers[i++] = x;
                }
                if (q_answers.contains(0)) {
                    quesSwitch.setChecked(false);
                } else {
                    quesSwitch.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     * Setting notification channeel
     * @param str
     */
    private void setChannel(String str) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        List<NotificationChannel> channelsList= notificationManager.getNotificationChannels();
        //check if  channel exists
        for (NotificationChannel nc: channelsList){
            if (nc.getId().equals(str)){ // found channel exists, dont need to add again
                return;
            }
        }

        //if we didnt return it means channel doesnt exists and needs to be created
        CharSequence name = str;
        String description = str;
        mDatabase.child("phone_notifications").setValue("yes");
        createNotificationChannel(str,name,description);

    }

    /**
     * Create notification channel
     * @param cid
     * @param name
     * @param description
     */
    private void createNotificationChannel(String cid, CharSequence name, String description) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(cid,name,importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        if (cid.equals("ques")){
            Log.d("TAG6", "createNotificationChannel: HELLO");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,21);
            calendar.set(Calendar.MINUTE,58);
            Intent ct1 = new Intent(getApplicationContext(), ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,ct1,0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.applyBtn:
                    if (phoneNotif.isChecked()){
                        mDatabase.child("phone_notifications").setValue("yes");
                        setChannel("ques");
                    }
                    else if (!phoneNotif.isChecked()){
                        mDatabase.child("phone_notifications").setValue("no");
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.deleteNotificationChannel("ques");
                    }
                SweetAlertDialog ad = new SweetAlertDialog(Settings.this, SweetAlertDialog.SUCCESS_TYPE);
                ad.setTitleText("Settings changed");
                ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        onBackPressed();
                    }
                });
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                break;

            case R.id.backBtn:
               onBackPressed();
                break;

            case R.id.fillTheQuestion:
                Intent intent = new Intent(Settings.this,Questionnaire.class);
                intent.putExtra("answers",answers);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    /**
     * Show previous values of switches
     */
    private void showPrevValues() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("phone_notifications").getValue().equals("yes")){
                    phoneNotif.setChecked(true);
                }
                else phoneNotif.setChecked(false);


                if(dataSnapshot.child("app_notifications").getValue().equals("yes")){
                    appNotif.setChecked(true);
                }
                else appNotif.setChecked(false);

                if(dataSnapshot.child("build_your_sch_reminders").getValue().equals("yes")){
                    buildYour.setChecked(true);
                }
                else buildYour.setChecked(false);

                if(dataSnapshot.child("google_calendar_sync").getValue().equals("yes")){
                    googleSync.setChecked(true);
                }
                else googleSync.setChecked(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
