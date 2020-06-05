package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Class that controls showing Anchor's popup with its functionality, implementing Popup interface
 */
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
    private int fromWhereTheAnchor;
    private DatabaseReference mDatabase;
    private ReminderType chosenReminder;
    private ReminderType chosenReminderEdited;
    private TaskEntity task;
    private TaskCategory taskCategory;
    private AnchorEntity anchorToPresent;
    private ReminderType reminderType;
    private int reminderInt;
    private RoundedImageView photoHere;
    private String year;
    private String day;
    private String month;
    String dateOfAnchor="";

    /**
     * this function controls what happens on creation of the activity
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_page_poup);
        this.definePopUpSize();
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.getString("AnchorKeyFromWeek")!=null) {
                    fromWhereTheAnchor = 0; // from week/schedule/dashboard/day
                    anchorKey = b.getString("AnchorKeyFromWeek");
                    dateOfAnchor = b.getString("date");
                }
                else {
                    anchorKey = b.getString("AnchorKey");
                    fromWhereTheAnchor = 1; // from month fragment
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        sTime=findViewById(R.id.showFrom);
        eTime=findViewById(R.id.showTo);
        photoHere=findViewById(R.id.photo_here);
        //disable all views
        this.disableViews();
        //set data for the view from the DB
        this.showDetails(fromWhereTheAnchor);

    }

    /**
     * Method implemented from Popup interface. Here we define the popup size and other attributes
     */
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

    /**
     * This function blocks the views at the background of the popup
     */
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

    }

    /**
     * Show the details of the item in the popup
     */

    public void showDetails(int fromWhereTheAnchor) {
        this.showImage();
        anchorToPresent = new AnchorEntity();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
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
                anchorToPresent.setDate((String) dataSnapshot.child(anchorKey).child("date").getValue());
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
    /**
     * shows the image of the item inside the popup
     */

    @Override
    public void showImage() {
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors").child(anchorKey).child("photoUri");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageURL = (String) dataSnapshot.getValue();
                try {
                    Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.BLACK).borderWidthDp(0).cornerRadiusDp(30).oval(false).build();
                    Picasso.get().load(imageURL).fit().centerCrop().transform(transformation).into(photoHere);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Controls the edit mode of the popup
     */

    @Override
    public void editMode() {
        //change the icon view from edit to delete
        editModeBtn.setImageResource(R.drawable.ic_delete_black_24dp);
        editModeBtn.setBackgroundResource(R.drawable.avatar_female2);

        SpinnerShow.setVisibility(View.INVISIBLE);

        //if it is in scheduke, disable date+time
        if (fromWhereTheAnchor==0)//not from month calendar
        {
             date.setEnabled(false);
             sTime.setEnabled(false);
             eTime.setEnabled(false);
        }
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
                        Log.d("TAG4", "onClick: " + date.getText());
                        deleteTaskFromDB(date.getText());
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
                    AnchorEntity anchorToChange = new AnchorEntity();
                    anchorToChange.setDescription(descriptionText.getText().toString());
                    anchorToChange.setLocation(locationText.getText().toString());
                    anchorToChange.setDate(date.getText().toString());
                    anchorToChange.setStartTime(sTime.getText().toString());
                    anchorToChange.setEndTime(eTime.getText().toString());

                    if (checkStartEndTime(anchorToChange)){
                        editAnchorInDB(anchorToChange);
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
            }
        });

//        date.setOnClickListener(v -> {
//            final Calendar c = Calendar.getInstance();
//            DatePickerDialog dpd = new DatePickerDialog(AnchorPagePopup.this, (view, year, month, dayOfMonth) -> {
//                String d = dayOfMonth<10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
//                String m = month+1<10 ? "0" + String.valueOf(month+1) : String.valueOf(month+1);
//                String str = d+"-"+m+"-"+year;
//                date.setText(str);
//            }
//                    ,c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//            dpd.show();
//      });

        sTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            // Get Current Time
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String min = minute>30? "30" : "00";
                            sTime.setText(hourOfDay + ":" + min);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        });

        eTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            // Get Current Time
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String min = minute>30? "30" : "00";
                            eTime.setText(hourOfDay + ":" + min);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        });



    }

    /**
     * This function check validity of start and end time , for example if end time before start time
     * @param anchorToChange
     * @return true if start and end times ar eok and false if not
     */
    private boolean checkStartEndTime(AnchorEntity anchorToChange) {
        String startTime = anchorToChange.getStartTime();
        String[] startArr = startTime.split(":");
        int startHour = Integer.parseInt(startArr[0]);
        int startMin = Integer.parseInt(startArr[1]);
        String endTime = anchorToChange.getEndTime();
        String[] endArr = endTime.split(":");
        int endHour = Integer.parseInt(endArr[0]);
        int endMin = Integer.parseInt(endArr[1]);
        if (startTime.trim().length() == 0) {
            SweetAlertDialog ad = new SweetAlertDialog(AnchorPagePopup.this, SweetAlertDialog.ERROR_TYPE);
            ad.setTitleText("Error");
            ad.setContentText("You must enter start time!");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);
            return false;
        }

        if (endTime.trim().length() == 0) {
            SweetAlertDialog ad = new SweetAlertDialog(AnchorPagePopup.this, SweetAlertDialog.ERROR_TYPE);
            ad.setTitleText("Error");
            ad.setContentText("You must enter end time!");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);
            return false;
        }

        if (endHour < startHour || (endHour == startHour && endMin < startMin)) {
            SweetAlertDialog ad = new SweetAlertDialog(AnchorPagePopup.this, SweetAlertDialog.ERROR_TYPE);
            ad.setTitleText("Error");
            ad.setContentText("Start time must be before end time!");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);
            return false;
        }
        return true;
    }

    /**
     * editing anchor in DB after aplly in edit mode
     * @param anchorToChange
     */
    private void editAnchorInDB(AnchorEntity anchorToChange) {
        String arr[] = anchorToChange.getDate().split("-");
         day = arr[0];
         month = arr[1];
        if (month.charAt(0)==('0')){
           month =  month.substring(1);
        }
         year = arr[2];
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").
                child(Globals.UID).child("Calendar").child(year).child(month).child(day);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() { //EDIT IN ANCHORS
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(anchorKey).child("description").setValue(anchorToChange.getDescription());
                mDatabase.child(anchorKey).child("location").setValue(anchorToChange.getLocation());
                mDatabase.child(anchorKey).child("date").setValue(anchorToChange.getDate());
                mDatabase.child(anchorKey).child("startTime").setValue(anchorToChange.getStartTime());
                mDatabase.child(anchorKey).child("endTime").setValue(anchorToChange.getEndTime());

                if(reminder_switch.isChecked()) {
                    if(chosenReminderEdited!=null) {
                        mDatabase.child(anchorKey).child("reminderType").setValue(chosenReminderEdited);
                    }
                }
                else
                {
                    mDatabase.child(anchorKey).child("reminderType").setValue(null);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (String.valueOf(ds.child("brother").getValue()).equals(anchorKey)){
                        ds.getRef().child("description").setValue(anchorToChange.getDescription());
                        ds.getRef().child("location").setValue(anchorToChange.getLocation());
                        ds.getRef().child("date").setValue(anchorToChange.getDate());
                        ds.getRef().child("startTime").setValue(anchorToChange.getStartTime());
                        ds.getRef().child("endTime").setValue(anchorToChange.getEndTime());

                    }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */ // edit in calendar - come back here when we need it
        DatabaseReference mDatabase3;
        Query q;
        if (dateOfAnchor!=null) //EDIT IN SHCEULDE
        {
            mDatabase3=FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(dateOfAnchor).child("schedule");
            q=mDatabase3.orderByChild("AnchorID").equalTo(anchorKey);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().child("description").setValue(anchorToChange.getDescription());
                        ds.getRef().child("location").setValue(anchorToChange.getLocation());
                        if (reminder_switch.isChecked()) {
                            if (chosenReminderEdited != null) {
                                ds.getRef().child("reminderType").setValue(chosenReminderEdited);
                            }
                        } else {
                            ds.getRef().child("reminderType").setValue(null);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    /**
     * Delete anchor from DB after press delete and cinfirming
     * @param date
     */
    private void deleteTaskFromDB(CharSequence date) {
        mDatabase.child(anchorKey).setValue(null);
        String arr[] = date.toString().split("-");
        String day = arr[0];
        String month = arr[1];
        if (month.charAt(0)==('0')){
            month =  month.substring(1);
        }
        String year = arr[2];
        /*
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").
                child(Globals.UID).child("Calendar").child(year).child(month).child(day);
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (String.valueOf(ds.child("brother").getValue()).equals(anchorKey)){
                        ds.getRef().setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/ //come back here when we need calendar

        DatabaseReference schRef;
        Query q;
        if (dateOfAnchor!=null) //EDIT IN SHCEULDE
        {
            schRef=FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(dateOfAnchor).child("schedule");
            q=schRef.orderByChild("AnchorID").equalTo(anchorKey);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        Intent intent = new Intent(AnchorPagePopup.this, Homepage.class);
        intent.putExtra("FromHomepage", "2");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    /**
     * defining reminders spinner
     */
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

    /**
     * onClick method, from View.OnClickListener interface
     * @param v
     */

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

    /**
     * adding animation to androids back button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }
}
