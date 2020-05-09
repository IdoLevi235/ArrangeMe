package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AnchorPagePopup extends AppCompatActivity implements Popup, View.OnClickListener{
    private ImageView applyBtn;
    private ImageView editModeBtn;
    private EditText descriptionText;
    private EditText  locationText;
    private String anchorKey;
    private TextView SpinnerShow;
    private Spinner spinnerReminder;
    private Switch reminder_switch;
    private Button date;
    private Button sTime;
    private Button eTime;
    private Uri image;
    private DatabaseReference mDatabase;
    private ReminderType chosenReminder;
    private ReminderType chosenReminderEdited;
    private TaskEntity task;
    private TaskCategory taskCategory;
    private AnchorEntity anchorToPresent;
    private ReminderType reminderType;
    private int reminderInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_page_poup);
        this.definePopUpSize();
        Bundle b = getIntent().getExtras();
        if(b != null)
            anchorKey = b.getString("AnchorKey");

        //define views
        applyBtn=findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);
        editModeBtn=findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);
        SpinnerShow = findViewById(R.id.SpinnerShow);
        descriptionText =findViewById(R.id.descriptionText);
        locationText =findViewById(R.id.locationText);
        spinnerReminder = (Spinner) findViewById(R.id.spinner);
        reminder_switch = (Switch) findViewById(R.id.reminder_switch);
        date = findViewById(R.id.showDate);
        date.setOnClickListener(this);
        sTime=findViewById(R.id.showFrom);
        sTime.setOnClickListener(this);
        eTime=findViewById(R.id.showTo);
        eTime.setOnClickListener(this);

        //disable all views
        this.disableViews();
        //set data for the view from the DB
        this.showDetails();

    }

    @Override
    public void definePopUpSize() {
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
        Log.d("TAG", "definePopUpSize: " + this);

    }

    @Override
    public void disableViews() {
        SpinnerShow.setEnabled(false);
        SpinnerShow.setClickable(false);

        descriptionText.setEnabled(false);
        descriptionText.setClickable(false);

        locationText.setEnabled(false);
        locationText.setClickable(false);

        reminder_switch.setClickable(false);

    }

    @Override
    public void showDetails() {
        anchorToPresent = new AnchorEntity();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // description
                anchorToPresent.setDescription((String) dataSnapshot.child(anchorKey).child("description").getValue());
                descriptionText.setText(anchorToPresent.getDescription());

                // location
                anchorToPresent.setLocation((String) dataSnapshot.child(anchorKey).child("location").getValue());
                locationText.setText(anchorToPresent.getLocation());

                // reminder
                String reminder = (String) dataSnapshot.child(anchorKey).child("reminderType").getValue();
                Log.d("reminder", "onDataChange: " + reminder);
                if (reminder != null) {
                    reminderInt = ReminderType.fromStringToInt(reminder);
                } else {
                    reminderInt = -1;
                }

                if (reminderInt == -1) {
                    SpinnerShow.setVisibility(View.GONE);
                    reminder_switch.setChecked(false);
                } else {
                    SpinnerShow.setVisibility(View.VISIBLE);
                    SpinnerShow.setText(ReminderType.betterString(reminder));
                    reminder_switch.setChecked(true);
                }

                //date
                anchorToPresent.setDate((String) dataSnapshot.child(anchorKey).child("createDate").getValue());
                date.setText(anchorToPresent.getDate());

                //from
                anchorToPresent.setStartTime((String) dataSnapshot.child(anchorKey).child("startTime").getValue());
                sTime.setText(anchorToPresent.getStartTime());

                //to
                anchorToPresent.setEndTime((String) dataSnapshot.child(anchorKey).child("endTime").getValue());
                eTime.setText(anchorToPresent.getEndTime());


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void showImage() {
        //todo in the future
    }

    @Override
    public void editMode() {
        //change the icon view from edit to delete
        editModeBtn.setImageResource(R.drawable.ic_delete_black_24dp);
        editModeBtn.setBackgroundResource(R.drawable.avatar_female2);

        SpinnerShow.setVisibility(View.INVISIBLE);

        //set description editable
        descriptionText.setEnabled(true);
        descriptionText.setClickable(true);
        descriptionText.requestFocus();
        descriptionText.setCursorVisible(true);

        //set location editable
        locationText.setEnabled(true);
        locationText.setClickable(true);

        //set reminder editable
        reminder_switch.setEnabled(true);
        reminder_switch.setClickable(true);


        if(reminder_switch.isChecked()==true)
        {
            spinnerReminder.setVisibility(View.VISIBLE);
            defineSpinner();
        }
        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    spinnerReminder.setVisibility(View.VISIBLE);
                    defineSpinner();
                }
                else {
                    spinnerReminder.setVisibility(View.INVISIBLE);
                }
            }
        });
        //when click on delete the task
        editModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog delete;
                delete =  new SweetAlertDialog( AnchorPagePopup.this, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(("Are you sure that you want to delete this anchor?"));
                delete.setConfirmText("I'm Sure");
                delete.setCancelText("Cancel");
                delete.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        deleteTaskFromDB();
                        //TODO: delete task from db
                    }
                });
                delete.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();
                    }

                });
                delete.show();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionText.length() == 0) {
                    SweetAlertDialog ad = new SweetAlertDialog(AnchorPagePopup.this, SweetAlertDialog.ERROR_TYPE);
                    ad.setTitleText("Error");
                    ad.setContentText("You must enter anchor description!");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                } else {
                    TaskEntity editedTaskToChange = new TaskEntity();
                    editedTaskToChange.setDescription(descriptionText.getText().toString());
                    editedTaskToChange.setLocation(locationText.getText().toString());
                    editAnchorInDB(editedTaskToChange);
                    SweetAlertDialog ad = new SweetAlertDialog(AnchorPagePopup.this, SweetAlertDialog.SUCCESS_TYPE);
                    ad.setTitleText("Great Job");
                    ad.setContentText("The anchor has been edited");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                    ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                        }
                    });
                }
            }
        });

//TODO: edit the photo (add/choose another photo).

    }

    private void editAnchorInDB(TaskEntity editedTaskToChange) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(anchorKey).child("description").setValue(editedTaskToChange.getDescription());
                mDatabase.child(anchorKey).child("location").setValue(editedTaskToChange.getLocation());
                if(reminder_switch.isChecked()) {
                    if(chosenReminderEdited!=null) {
                        mDatabase.child(anchorKey).child("reminderType").setValue(chosenReminderEdited);
                    }
                }
                else
                {
                    mDatabase.child(anchorKey).child("reminderType").setValue(null);
                }


                //TODO: change photo in DB here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteTaskFromDB() {
        mDatabase.child(anchorKey).setValue(null);
        Intent intent = new Intent(AnchorPagePopup.this, Homepage.class);
        intent.putExtra("FromHomepage", "1");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    private void defineSpinner() {
        // Initializing a String Array
        String[] reminderItems = new String[]{
                "Select Reminder",
                "5 minutes before",
                "15 minutes before",
                "1 hour before",
                "1 day before"
        };
        final List<String> reminderItemsList = new ArrayList<>(Arrays.asList(reminderItems));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner,reminderItemsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {

                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(locationText.getHintTextColors());
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerReminder.setAdapter(spinnerArrayAdapter);
        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position>0) {
                    chosenReminderEdited = ReminderType.fromInt(position);
                }
                else if (position==0 && reminderInt==-1 )
                {
                    chosenReminderEdited=null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.applyBtn:
                onBackPressed();
            break;
            case R.id.editModeBtn:
                this.editMode();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }
}
