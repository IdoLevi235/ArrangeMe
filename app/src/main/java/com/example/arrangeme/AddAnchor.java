package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.ui.calendar.month.MonthFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddAnchor extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST_CODE = 1;
    private Button submit;
    private EditText desc;
    private Switch show_spinner;
    private Button addPhoto;
    private EditText addLocation;
    private ReminderType chosenReminder;
    private TaskCategory chosenCat;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private TextView textViewHelloAdd;
    private AnchorEntity anchorToAdd;
    private DatePicker datePicker;
    private Spinner spinnerRem;
    private Spinner spinnerCat;
    private Button time_start;
    private Button time_end;
    private Toolbar toolbar;
    private StorageReference mStorageRef;
    private Uri selectedImage2;
    private int currKey;
    private int year;
    private int month;
    private int day;
    private static int currKey2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anchor);
        toolbar = findViewById(R.id.toolbar_addAnchor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.backsmall);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i =  getIntent();
        String pickedDate = i.getStringExtra("date");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        desc = findViewById(R.id.desc_text_anchor);
        addPhoto = findViewById(R.id.add_photo1);
        addPhoto.setOnClickListener(this);
        addLocation = (EditText) findViewById(R.id.locationAnc11);
        anchorToAdd = new AnchorEntity();
        textViewHelloAdd = findViewById(R.id.textViewHelloAddanchor);
        textViewHelloAdd.setText("Hello, " + Globals.currentUsername + "!");
        addLocation = (EditText)findViewById(R.id.locationAnc11);

        /* Reminder spinner stuff */
        spinnerRem = (Spinner) findViewById(R.id.spinner_reminder);
        spinnerRem.setVisibility(View.INVISIBLE);
        String[] reminderItems = new String[]{
                "Select Reminder",
                "5 minutes before",
                "15 minutes before",
                "1 hour before",
                "1 day before"
        };
        setSpinner(spinnerRem, reminderItems);
        /* Reminder spinner stuff end */

        /* Category spinner stuff */
        spinnerCat=(Spinner)findViewById(R.id.spinner_cat);
        String[] categoryItems = new String[]{
                "Select Category",
                "Study",
                "Sport",
                "Work",
                "Nutrition",
                "Family",
                "Chores",
                "Relax",
                "Friends",
                "Other"
        };
        setSpinner(spinnerCat, categoryItems);
        /* Category spinner stuff end */

        /* Toggle stuff */
        show_spinner = (Switch) findViewById(R.id.reminder_switch);
        setToggle(show_spinner);
        /* Toggle stuff End*/

        /* Date Picker */
        datePicker = findViewById(R.id.datePicker);
        setDatePicker(datePicker,pickedDate);
        /* Date Picker End*/

        /* Time picker stuff */
        time_start = (Button)findViewById(R.id.fromBtn);
        time_end = (Button)findViewById(R.id.toBtn);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        /* Time picker stuff end */

        /* Submit button stuff */
        submit = findViewById(R.id.sumbitBtn11);
        submit.setOnClickListener(this);
        /* Submit button stuff end*/


    }

    private void setDatePicker(DatePicker datePicker, String pickedDate) {
        // pickedDate string is in format of dd-mm-yyyy
        try {
            String[] arr = pickedDate.split("-",3);
            int day = Integer.parseInt(arr[0]);
            int month = Integer.parseInt(arr[1])-1;
            int year = Integer.parseInt(arr[2]);
            datePicker.updateDate(year, month, day);
        }
        catch (NullPointerException e){
            //if no date picked, we catch the exception and continue like default, it puts today's date
        }
    }

    private void setToggle(Switch show_spinner) {
        show_spinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerRem.setVisibility(View.VISIBLE);
                } else {
                    spinnerRem.setVisibility(View.INVISIBLE);

                }
            }
        });

    }

    private void setSpinner(Spinner spinner, String[] Items) {
        // Initializing a String Array
        final List<String> reminderItemsList = new ArrayList<>(Arrays.asList(Items));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.item_spinner, reminderItemsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(desc.getHintTextColors());
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // Notify the selected item text
                    if (spinner==spinnerRem)
                    chosenReminder = ReminderType.fromInt(position);
                    else if (spinner==spinnerCat){
                    chosenCat=TaskCategory.fromInt(position);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Button addPhoto = (Button) findViewById(R.id.add_photo1);
                    Uri selectedImage = data.getData();
                    selectedImage2=selectedImage;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.add_task_round));
                        addPhoto.setHint("");
                        addPhoto.setCompoundDrawables(null, null, null, null);
                        addPhoto.setBackground(d);

                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        addPhoto.setBackground(d);
                    }
                    break;
            }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        // Toast.makeText(AddTasks.this, "Settings clicked", Toast.LENGTH_LONG).show();
        // return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.add_photo1):
                pickFromGallery();
                break;
            case (R.id.fromBtn):
                showTimePickerDialog(time_start);
                time_end.setEnabled(true);
                break;
            case (R.id.toBtn):
                showTimePickerDialog(time_end);
                break;
            case(R.id.sumbitBtn11):
                anchorToAdd = new AnchorEntity();
                anchorToAdd.setDescription(desc.getText().toString());
                anchorToAdd.setReminderType(chosenReminder);

                anchorToAdd.setCategory(chosenCat);
                String date = generateDateStringFromDatepicker(datePicker);
                anchorToAdd.setDate(date);
                anchorToAdd.setStartTime((String) time_start.getText());
                anchorToAdd.setLocation(addLocation.getText().toString());
                anchorToAdd.setEndTime((String) time_end.getText());
                if (validateForm(anchorToAdd)) {
                    addAnchorToDB(anchorToAdd);
                    sendImage(selectedImage2);
                    showSuccessAlert();
                }
                break;
            default:
                break;
        }
    }

    private void sendImage(Uri selectedImage2) {
        String uniqueID = UUID.randomUUID().toString();
        StorageReference imgRef = mStorageRef.child("images/anchors/"+Globals.UID+"/"+uniqueID+".jpg");
        try {
            UploadTask uploadTask = imgRef.putFile(selectedImage2);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        addPhotoUriToDB(downloadUri);
                        Log.d("TAG8", "onComplete: down: " + downloadUri);
                    } else {
                        Log.d("TAG8", "onComplete: Download link generation failed");
                    }
                }
            });
        }    catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addPhotoUriToDB(Uri downloadUri) {
        Log.d("TAG8", "addPhotoUriToDB: " + currKey + " " + downloadUri);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").
                child(Globals.UID).child("Calendar").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day))
                .child(String.valueOf(currKey2));
        mDatabase.child("photoUri").setValue(downloadUri.toString());
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors").child(String.valueOf(currKey));
        mDatabase2.child("photoUri").setValue((downloadUri.toString()));


    }

    private boolean validateForm(AnchorEntity anchorToAdd) {

        try {
            String startTime = anchorToAdd.getStartTime();
            String[] startArr = startTime.split(":");
            int startHour = Integer.parseInt(startArr[0]);
            int startMin = Integer.parseInt(startArr[1]);
            String endTime = anchorToAdd.getEndTime();
            String[] endArr = endTime.split(":");
            int endHour = Integer.parseInt(endArr[0]);
            int endMin = Integer.parseInt(endArr[1]);
            Log.d("TAG", "validateForm: " +startHour + startMin + endHour + endMin);
            if (anchorToAdd.getDescription().trim().length() == 0) {
                SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter description!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                return false;
            }

            if (startTime.trim().length() == 0) {
                SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter start time!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                return false;
            }

            if (endTime.trim().length() == 0) {
                SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter end time!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                return false;
            }

            if (endHour < startHour || (endHour == startHour && endMin < startMin)) {
                SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("Start time must be before end time!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                return false;
            }

            return true;
        } catch (Exception e){
            SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.ERROR_TYPE);
            ad.setTitleText("Error");
            ad.setContentText("You must insert Start time and end time!");
            ad.show();
            Button btn = (Button) ad.findViewById(R.id.confirm_button);
            btn.setBackgroundResource(R.drawable.rounded_rec);
            return false;

        }
    }

    private void showSuccessAlert() {
        SweetAlertDialog ad = new SweetAlertDialog(AddAnchor.this, SweetAlertDialog.SUCCESS_TYPE);
        ad.setTitleText("Great!");
        ad.setContentText("You added new Anchor!");
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);
        Intent intent = new Intent(this, Homepage.class);
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
              intent.putExtra("FromHomepage", "2");
              startActivity(intent);

            }
        });

    }

    private void addAnchorToDB(AnchorEntity anchorToAdd) {
        String[] arr = anchorToAdd.getDate().split("-",3);
         day = Integer.parseInt(arr[0]);
         month = Integer.parseInt(arr[1]);
         year = Integer.parseInt(arr[2]);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).
                child("Calendar").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day));
        Query lastQuery = mDatabase2.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String biggestKey=null;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    biggestKey = ds.getKey();
                }
                int newKey;
                if (biggestKey==null) newKey=0;
                else newKey = Integer.parseInt(biggestKey) + 1;
                currKey2=newKey;
                mDatabase2.child(String.valueOf(newKey)).setValue(anchorToAdd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
        Query lastQuery2 = mDatabase.orderByKey().limitToLast(1);
        lastQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String biggestKey=null;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    biggestKey = ds.getKey();
                }
                int newKey;
                if (biggestKey==null) newKey=0;
                else newKey = Integer.parseInt(biggestKey) + 1;
                currKey=newKey;
                mDatabase.child(String.valueOf(newKey)).setValue(anchorToAdd);
                mDatabase2.child(String.valueOf(currKey2)).child("brother").setValue(currKey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private String generateDateStringFromDatepicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        if(day<10){
            sb.append(0);
        }
        sb.append(day);
        sb.append('-');
        if (month+1<10){
            sb.append(0);
        }
        sb.append(month+1);
        sb.append('-');
        sb.append(year);
        return (sb.toString());
    }

    private void showTimePickerDialog(Button btn) {

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
                        btn.setText(hourOfDay + ":" + min);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

}


