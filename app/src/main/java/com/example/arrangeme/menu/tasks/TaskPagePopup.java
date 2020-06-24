package com.example.arrangeme.menu.tasks;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.Popup;
import com.example.arrangeme.R;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Task popup class
 */
public class TaskPagePopup extends Activity  implements View.OnClickListener, Popup {

    private ImageView applyBtn;
    private ImageView editModeBtn;
    private EditText descriptionText;
    private EditText  locationText;
    private TextView textCategory;
    private TextView SpinnerShow;
    private Spinner spinnerReminder;
    private Switch reminder_switch;
    private Uri image;
    private DatabaseReference mDatabase;
    private DatabaseReference schRef;
    private ReminderType chosenReminder;
    private ReminderType chosenReminderEdited;
    private TaskEntity task;
    private String taskKey;
    private TaskEntity taskToPresent;
    private TaskCategory taskCategory;
    private ReminderType reminderType;
    private int fromWhereTheTask;
    private int reminderInt;
    private RoundedImageView photoHere;
    private String date;
    private String flagPhoto;
    private  RelativeLayout relativeLayout;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskpagepopup);

        //task key is the key for the task that the user touched
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.getString("TaskKeyFromWeek") != null) { // from calendars/dashboard/schedule
                    fromWhereTheTask = 0;
                    String str = b.getString("TaskKeyFromWeek");
                    taskKey = str.substring(4);
                    flagPhoto = b.getString("photo");
                    date = b.getString("date");
                } else { // from tasks tab
                    taskKey = b.getString("TaskKey");
                    flagPhoto = b.getString("photo");
                    fromWhereTheTask = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        relativeLayout = findViewById(R.id.relativeLayout6);
        relativeLayout.setOnClickListener(this);
        definePopUpSize1(flagPhoto);

        //define buttons
        applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(this);
        editModeBtn = findViewById(R.id.editModeBtn);
        editModeBtn.setOnClickListener(this);
        SpinnerShow = findViewById(R.id.SpinnerShow);
        descriptionText = findViewById(R.id.descriptionText);
        locationText = findViewById(R.id.locationText);
        textCategory = findViewById(R.id.textCategory);
        spinnerReminder = (Spinner) findViewById(R.id.spinner);
        reminder_switch = (Switch) findViewById(R.id.reminder_switch);
        photoHere = findViewById(R.id.photo_here);

        //disable all views
        this.disableViews();

        //set data for the view from the DB
        this.showDetail(fromWhereTheTask);

        // if(Globals.isNewUser==true) {
        tutorial();
        //}
    }

    public void tutorial() {
        if (Globals.tutorial == 4) {
            Globals.tutorial++;
            new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.editModeBtn).setTextGravity(Gravity.RIGHT).setPrimaryText("Click to edit tasks details").setTextGravity(Gravity.CENTER).setBackgroundColour(Color.parseColor("#20666E")).show();
        }
    }


    /**
     * set data to the views from the DB
     * shows the task's details in the activity layou
     * @param fromWhereTheTask
     */

    public void showDetail(int fromWhereTheTask) {
        TaskEntity taskToPresent =new TaskEntity();
        if(fromWhereTheTask==0) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Active_tasks");
        }
        else if (fromWhereTheTask==1){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
        }
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //description
                taskToPresent.setDescription((String) dataSnapshot.child(taskKey).child("description").getValue());
                descriptionText.setText(taskToPresent.getDescription());

                //location
                taskToPresent.setLocation((String) dataSnapshot.child(taskKey).child("location").getValue());
                locationText.setText(taskToPresent.getLocation());

                //category
                String category = (String) dataSnapshot.child(taskKey).child("category").getValue();
                int x = taskCategory.fromStringToInt(category);
                Integer[] catIcon = {R.drawable.study, R.drawable.sport, R.drawable.work, R.drawable.nutrition, R.drawable.familycat, R.drawable.chores, R.drawable.relax, R.drawable.friends_cat, 0};
                Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};
                textCategory.setText(category);
                textCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), catColor[x]));
                textCategory.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, catIcon[x], 0);
                textCategory.setPadding(0, 0, 20, 0);

                //reminder
                String reminder = (String) dataSnapshot.child(taskKey).child("reminderType").getValue();
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


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        this.showImage();

    }

    /**
     * Show task's image in the popup
     */
    @Override
    public void showImage(){
        try{
            DatabaseReference mDatabase2 = null;
            if(fromWhereTheTask==0) {
                 mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Active_tasks").child(taskKey).child("photoUri");
            }
            else if (fromWhereTheTask==1){
                 mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks").child(taskKey).child("photoUri");
            }
            mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageURL = (String) dataSnapshot.getValue();
                try {
                    Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.BLACK).borderWidthDp(0).cornerRadiusDp(30).oval(false).build();
                    Picasso.get().load(imageURL).fit().centerCrop().transform(transformation).into(photoHere);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * turn on the edit mode
     */
    public void editMode() {
        //change the icon view from edit to delete
        editModeBtn.setImageResource(R.drawable.ic_delete_black_24dp);
        editModeBtn.setBackgroundResource(R.drawable.circle_avatar3);

        SpinnerShow.setVisibility(View.GONE);

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
                    spinnerReminder.setVisibility(View.GONE);
                }
            }
        });

        textCategory.setOnClickListener(this);
        //when click on delete the task
        editModeBtn.setOnClickListener(new View.OnClickListener() { //deleting here
           @Override
           public void onClick(View v) {
               SweetAlertDialog delete;
               delete =  new SweetAlertDialog( TaskPagePopup.this, SweetAlertDialog.WARNING_TYPE)
                       .setContentText(("Are you sure that you want to delete this task?"));
               delete.setConfirmText("I'm Sure");
               delete.setCancelText("Cancel");
               delete.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sDialog) {
                       deleteTaskFromDB();
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
        applyBtn.setOnClickListener(new View.OnClickListener() { //editing here
            @Override
            public void onClick(View v) {
                if (descriptionText.length() == 0) {
                    SweetAlertDialog ad = new SweetAlertDialog(TaskPagePopup.this, SweetAlertDialog.ERROR_TYPE);
                    ad.setTitleText("Error");
                    ad.setContentText("You must enter task description!");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                } else {
                    TaskEntity editedTaskToChange = new TaskEntity();
                    editedTaskToChange.setDescription(descriptionText.getText().toString());
                    editedTaskToChange.setLocation(locationText.getText().toString());
                    addTaskToDB(editedTaskToChange);
                    SweetAlertDialog ad = new SweetAlertDialog(TaskPagePopup.this, SweetAlertDialog.SUCCESS_TYPE);
                    ad.setTitleText("Great Job");
                    ad.setContentText("The task has been edited");
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

    }


    /**
     * Define spinner of reminders
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
     * Make edits in the DB.
     * Divide to edit tasks from schedules OR edit tasks that havent been assigned to schedules yet
     * @param editedTaskToChange
     */
    private void addTaskToDB(TaskEntity editedTaskToChange) { // editing here
        if(fromWhereTheTask==0){ // update in active tasks + sch
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Active_tasks");
            schRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(date).child("schedule");
            updateTaskInSchedule(editedTaskToChange);
        }
        else if (fromWhereTheTask==1) { // update only in pending tasks
             mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
        }
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   mDatabase.child(taskKey).child("description").setValue(editedTaskToChange.getDescription());
                   mDatabase.child(taskKey).child("location").setValue(editedTaskToChange.getLocation());
                    if(reminder_switch.isChecked()) {
                        if(chosenReminderEdited!=null) {
                            mDatabase.child(taskKey).child("reminderType").setValue(chosenReminderEdited);
                        }
                    }
                    else
                    {
                        mDatabase.child(taskKey).child("reminderType").setValue(null);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    /**
     * edit tasks that are assigned to schedule
     * @param editedTaskToChange
     */
    private void updateTaskInSchedule(TaskEntity editedTaskToChange) {
        String activeKey = "2305" + taskKey;
        Query q = schRef.orderByChild("activeKey").equalTo(activeKey);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().child("description").setValue(editedTaskToChange.getDescription());
                    ds.getRef().child("location").setValue(editedTaskToChange.getLocation());
                    if(reminder_switch.isChecked()) {
                        if(chosenReminderEdited!=null) {
                            ds.getRef().child("reminderType").setValue(chosenReminderEdited);
                        }
                    }
                    else
                    {
                        ds.getRef().child("reminderType").setValue(null);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Delete task with popup delete button
     */
    private void deleteTaskFromDB() {
        // delete from active tasks/pending tasks
        mDatabase.child(taskKey).setValue(null);
        Intent intent = new Intent(TaskPagePopup.this, Homepage.class);
        intent.putExtra("FromHomepage", "1");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if(fromWhereTheTask==0) {
            schRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(date).child("schedule");
            deleteTaskFromSchedule();
        }

    }

    /**
     * Delete task that is assigned to schedule with popup button
     */
    private void deleteTaskFromSchedule() {
        String activeKey = "2305" + taskKey;
        Query q = schRef.orderByChild("activeKey").equalTo(activeKey);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * return o task tab
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * @param v
     */
    //onclick for the show task popup
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.applyBtn):
                onBackPressed();
                break;
            case (R.id.editModeBtn):
                this.editMode();
                break;
            case (R.id.textCategory):
                SweetAlertDialog ad = new SweetAlertDialog(TaskPagePopup.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You can't edit a task category. You can delete the task and add it again. ");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                break;
            default:
                onBackPressed();
                break;
        }
    }

    /**
     * Define popup size - with photo
     * @param flagPhoto
     */
    public void definePopUpSize1(String flagPhoto) {
        if(flagPhoto.equals("yes"))
        {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            getWindow().setLayout((int) (width *0.9 ), (int) (height *0.84));
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
        else{

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            lp= new RelativeLayout.LayoutParams(lp.width, 950);
            lp.setMarginStart(55);
            Log.d("popupsize", "lp: "+lp.height);
            lp.topMargin=150;
            relativeLayout.setTranslationY(0);
            relativeLayout.setLayoutParams(lp);
            relativeLayout.setFocusable(true);
            relativeLayout.setElevation(10);


            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            getWindow().setLayout((int) (width *0.9 ), (int) (height *0.55));
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.dimAmount = 0.5f;
            params.gravity = Gravity.CENTER;
            params.x = 0;
            params.y = -20;
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            this.setFinishOnTouchOutside(false);
            relativeLayout.setBackgroundColor(R.drawable.themecorners);
            relativeLayout.setBackgroundResource(R.drawable.themecorners);
        }

    }


    /**
     *
     */
    @Override
    public void definePopUpSize() {
    }

    /**
     * Disable background  views behind popup
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

    /**
     *
     */
    @Override
    public void showDetails() {

    }
}
