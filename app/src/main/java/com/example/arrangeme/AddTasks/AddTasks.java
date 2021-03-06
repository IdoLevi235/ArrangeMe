package com.example.arrangeme.AddTasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.example.arrangeme.Globals;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Add Tasks class - here the user can insert new tasks.
 * Those tasks will be assigned into schedule later
 */
public class AddTasks extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private FirebaseFunctions mFunctions;
    private static final int GALLERY_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ArrayList<MainModel> mainModels;
    private MainAdapter mainAdapter;
    private Toolbar toolbar;
    private Button rightScrl;
    private Button leftScrl;
    private Button confirmBtn;
    private int currentPosition;
    private final int numOfCategories = 9;
    private EditText desc;
    private Switch show_spinner;
    private RoundedImageView addPhoto;
    private EditText addLocation;
    private ImageView photo;
    private TaskEntity taskEntityToAdd;
    private ReminderType chosenReminder;
    private Uri selectedImage2;
    private final int[] newKey2 = new int[1];
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private String currentDate;
    private StorageReference mStorageRef;
    private int currKey;
    private Uri downloadUri;

    /**
     * @param savedInstanceState
     * onCreate method - set the initial parameters to all of xml components in this page
     */
    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        toolbar = findViewById(R.id.toolbar_addTasks);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_40);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFunctions = FirebaseFunctions.getInstance();
        desc=findViewById(R.id.desc_text);
        addPhoto=findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(this);
        addPhoto.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
        addPhoto.setPadding(100,100,102,100);
        addLocation=(EditText)findViewById(R.id.locationBtn);
        leftScrl=findViewById(R.id.btnLeftScrl);
        rightScrl=findViewById(R.id.btnRightScrl);
        taskEntityToAdd =new TaskEntity();
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        /* Recycler View Stuff */
        recyclerView = findViewById(R.id.recycler_view);
        setRecyclerView(recyclerView);
        /* Recycler View Stuff End*/

        /*description stuff*/
        /*description stuff end*/

        /* Right and Left click listenrs */
        rightScrl.setOnTouchListener(this);
        leftScrl.setOnTouchListener(this);
        /* Right and Left click listenrs end*/


        /* spinner stuff */
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);
        // Initializing a String Array
        String[] reminderItems = new String[]{
                "Select Reminder",
                "5 minutes before",
                "15 minutes before",
                "1 hour before",
                "1 day before"
        };
        setSpinner(spinner,reminderItems);
        /* spinner stuff end */


        /* Toggle stuff */
        show_spinner = (Switch)findViewById(R.id.reminder_switch);
        show_spinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    spinner.setVisibility(View.VISIBLE);
                }
                else {
                    spinner.setVisibility(View.INVISIBLE);

                }
            }
        });
        /* Toggle stuff End*/



        /* confirm button click listener */
        confirmBtn=findViewById(R.id.sumbitBtn11);
        confirmBtn.setOnClickListener(this);
        /* confirm button click listener end*/

        tutorial();
    }


    /**
     * defining the tutorial in the start of the app
     */
    private void tutorial() {
        //this function is the help system
         if(Globals.isNewUser==true) {
        if(Globals.tutorial==2) {
            Globals.tutorial++;
            new MaterialTapTargetPrompt.Builder(this).setTextGravity(Gravity.RIGHT).setTarget(R.id.add_photo).setPrimaryText("Upload a task photo").setSecondaryText("You can even upload a photo to your task!").setBackgroundColour(Color.parseColor("#20666E")).show();
        }

        }
    }

    /**
     * @param spinner
     * @param items
     * this functions set the spinner with reminder type options
     */

    private void setSpinner(Spinner spinner, String[] items) {
        final List<String> reminderItemsList = new ArrayList<>(Arrays.asList(items));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.item_spinner,reminderItemsList){
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
                    tv.setTextColor(desc.getHintTextColors());
                }
                else {
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
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){
                    // Notify the selected item text
                    chosenReminder = ReminderType.fromInt(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    /**
     * @param recyclerView
     * This function sets the recycler view of the categories.
     */
    private void setRecyclerView(RecyclerView recyclerView) {
        Integer[] catIcon = {R.drawable.study, R.drawable.sport,  R.drawable.work, R.drawable.nutrition,
                R.drawable.familycat, R.drawable.chores, R.drawable.relax,R.drawable.friends_cat, 0};
        String[] catName = {"Study", "Sport", "Work", "Nutrition", "Family", "Chores", "Relax", "Friends","Other"};
        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport,
                R.drawable.category_btn_work, R.drawable.category_btn_nutrition,
                R.drawable.category_btn_family,R.drawable.category_btn_chores,
                R.drawable.category_btn_relax, R.drawable.category_btn_friends, R.drawable.category_btn_other};
        Integer[] catBackgroundFull =
                {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                        R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                        R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                        R.drawable.rounded_rec_relax_nostroke,R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_other_nostroke};
        Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
                R.color.family, R.color.chores, R.color.relax,R.color.friends, R.color.other};
        mainModels = new ArrayList<>();
        for (int i = 0; i < catIcon.length; i++) {
            MainModel model = new MainModel(catIcon[i], catName[i], catBackground[i],catColor[i],catBackgroundFull[i]);
            Log.d("TAG", "onCreate: " + catIcon[i]);
            mainModels.add(model);
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(AddTasks.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(AddTasks.this, mainModels);
        recyclerView.setAdapter(mainAdapter);
        leftScrl.setVisibility(View.INVISIBLE);
        rightScrl.setVisibility(View.VISIBLE);
        rightScrl.setBackgroundResource(0);
        leftScrl.setBackgroundResource(0);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    //onPageChanged(position);
                    SwitchArrows();
                }
                else if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                    SwitchArrows();
                }
                System.err.println(currentPosition);
            }
        });


    }


    /**
     * This function inflates the top toolbar
     * @param menu
     * @return true if the toolbar created successfully
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }
    /**
     *   this function handles action bar item clicks
     *   @param item
     * @return true if the toolbar created successfully
     */

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


    /**
     * Hiding/showing navigation arrows of recycler view
     */
    public void SwitchArrows(){
        leftScrl=findViewById(R.id.btnLeftScrl);
        rightScrl=findViewById(R.id.btnRightScrl);
        if (currentPosition == 0) {
            leftScrl.setVisibility(View.INVISIBLE);
            rightScrl.setVisibility(View.VISIBLE);
        } else if (currentPosition == numOfCategories - 3) {
            leftScrl.setVisibility(View.VISIBLE);
            rightScrl.setVisibility(View.INVISIBLE);
        } else {
            leftScrl.setVisibility(View.VISIBLE);
            rightScrl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * onClick listener
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.add_photo):
                pickFromGallery();
                break;
            case(R.id.sumbitBtn11):
                addTaskToDB();
                sendImage(selectedImage2);
            default:
                break;
        }
    }

    /**
     * @param selectedImage2
     * With given Uri , we upload the photo to Firebase storage
     */
    private void sendImage(Uri selectedImage2) {
        String uniqueID = UUID.randomUUID().toString();
        StorageReference imgRef = mStorageRef.child("images/tasks/"+Globals.UID+"/"+uniqueID+".jpg");
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
                downloadUri = task.getResult();
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

    /**
     * @param downloadUri
     * adding photoUri to DB
     */
    private void addPhotoUriToDB(Uri downloadUri) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks").child(String.valueOf(currKey));
        mDatabase.child("photoUri").setValue(downloadUri.toString());
    }

    /**
     * Adding new tas to db
     * includes form validations, adding to DB and success alertbox
     */
    private void addTaskToDB() {

            String description = desc.getText().toString();
            String location = addLocation.getText().toString();
            if(mainAdapter.getCurrentCategory()==null) { //if no category picked
                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must choose a category!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }

            else if (description.trim().length() == 0){
                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter task description!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }

            else {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
                LocalDateTime now = LocalDateTime.now();
                String year = Integer.toString(now.getYear());
                String month = Integer.toString(now.getMonthValue());
                String day = Integer.toString(now.getDayOfMonth());

                mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Calender").child(year).child(month).child(day);
                Query lastQuery = mDatabase.orderByKey().limitToLast(1);
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
                        currKey = newKey;
                        taskEntityToAdd.setCategory(mainAdapter.getCurrentCategory());
                        taskEntityToAdd.setDescription(description);
                        taskEntityToAdd.setReminderType(chosenReminder);
                        taskEntityToAdd.setLocation(location);
                        taskEntityToAdd.setCreateDate(currentDate);
                        mDatabase.child(String.valueOf(newKey)).setValue(taskEntityToAdd);
                        mDatabase.child(String.valueOf(newKey)).child("type").setValue("TASK");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.SUCCESS_TYPE);
                ad.setTitleText("Great!");
                ad.setContentText("You added new task!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
                Intent intent = new Intent(this, Homepage.class);
                ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        intent.putExtra("FromHomepage", "1");
                        startActivity(intent);
                    }
                });
            }

    }

    /**
     * Picking photo from phone's gallery to upload
     */
    private void pickFromGallery() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     *
     */
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Button addPhoto2 = (Button)findViewById(R.id.add_photo);
                    Uri selectedImage = data.getData();
                    selectedImage2=selectedImage;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.add_task_round));
                        addPhoto2.setHint("");
                        addPhoto2.setCompoundDrawables(null,null,null,null);
                        addPhoto.setBackground(d);

                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        addPhoto.setBackground(d);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == Activity.RESULT_OK) {
                        selectedImage2 = result.getUri();
                        try {
                            RoundedImageView addPhoto = findViewById(R.id.add_photo);
                            addPhoto.setPadding(0,0,0,0);

                            Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(20).oval(false).build();
                            Picasso.get().load(selectedImage2.toString()).noFade().fit().centerCrop().transform(transformation).into(addPhoto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;
            }

    }

    /**
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case (R.id.btnRightScrl):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recyclerView.smoothScrollBy(300, 0);
                    rightScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    rightScrl.setBackgroundResource(0);
                }
                return true;

            case (R.id.btnLeftScrl):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recyclerView.smoothScrollBy(-300, 0);
                    leftScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    leftScrl.setBackgroundResource(0);
                }
                return true;

            default:
                break;
        }
        return false;
    }
}
