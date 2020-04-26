package com.example.arrangeme.AddTasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.MainActivity;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.schedule.ScheduleFragment;
import com.example.arrangeme.ui.tasks.TasksFragment;
import com.google.android.material.circularreveal.CircularRevealWidget;

import java.io.FileNotFoundException;
import java.io.InputStream;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class AddTasks extends AppCompatActivity implements View.OnClickListener {
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
    private TextView textViewHelloAdd;
    private EditText desc;
    private Switch show_spinner;
    private Button addPhoto;
    private EditText addLocation;
    private ImageView photo;
    private TaskEntity taskEntityToAdd;
    private ReminderType chosenReminder;
    private Uri selectedImage;
    private DatabaseReference mDatabase;

    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar_addTasks);
        mFunctions = FirebaseFunctions.getInstance();
        setSupportActionBar(toolbar);
        leftScrl=findViewById(R.id.btnLeftScrl);
        rightScrl=findViewById(R.id.btnRightScrl);
        confirmBtn=findViewById(R.id.sumbitBtn11);
        desc=findViewById(R.id.desc_text);
        addPhoto=findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(this);
        addLocation=(EditText)findViewById(R.id.locationBtn);
        //photo=findViewById(R.id.photo);
        //photo.setVisibility(View.INVISIBLE);
        taskEntityToAdd =new TaskEntity();

        /* Recycler View Stuff */
        recyclerView = findViewById(R.id.recycler_view);
        Integer[] catIcon = {R.drawable.study, R.drawable.study,  R.drawable.work, R.drawable.study,
                R.drawable.familycat, R.drawable.chores, R.drawable.study, R.drawable.friends_cat, 0};
        String[] catName = {"Study", "Sport", "Work", "Nutrition", "Family", "Chores", "Relax", "Friends","Other"};
        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport,
                R.drawable.category_btn_work, R.drawable.category_btn_nutrition,
                R.drawable.category_btn_family, R.drawable.category_btn_chores,
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

        /* Recycler View Stuff End*/

        /*description stuff*/
        //desc.addTextChangedListener(filterTextWatcher);
        //todo: when text typed change pencil to green
        /*description stuff end*/

        /* Right and Left click listenrs */
        rightScrl.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerView.smoothScrollBy(300, 0);
                rightScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                rightScrl.setBackgroundResource(0);
            }
            return true;
        });

        leftScrl.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerView.smoothScrollBy(-300, 0);
                leftScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                leftScrl.setBackgroundResource(0);
            }
            return true;
        });
        /* Right and Left click listenrs end*/
        textViewHelloAdd = findViewById(R.id.textViewHelloAdd);
        textViewHelloAdd.setText("Hello, " + Globals.currentUsername + "!");

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
        final List<String> reminderItemsList = new ArrayList<>(Arrays.asList(reminderItems));
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
        confirmBtn.setOnClickListener(v -> {
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

            if (description.trim().length() == 0){
                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter task description!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }

              else {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
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
                        taskEntityToAdd.setCategory(mainAdapter.getCurrentCategory());
                        taskEntityToAdd.setDescription(description);
                        taskEntityToAdd.setReminderType(chosenReminder);
                        taskEntityToAdd.setPhoto(selectedImage);
                        taskEntityToAdd.setLocation(location);
                        mDatabase.child(String.valueOf(newKey)).setValue(taskEntityToAdd);



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
                ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                    }
                });
              }
        });
        /* confirm button click listener end*/

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.add_photo):
                pickFromGallery();
                break;
            default:
                break;
        }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Button addPhoto = (Button)findViewById(R.id.add_photo);
                    //addPhoto.setText("Photo selected!");
                    //addPhoto.setTextColor(Color.parseColor("#3b9453"));
                    //addPhoto.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.greencheckmark24, 0);
                    Uri selectedImage = data.getData();
                    //ImageView photo=findViewById(R.id.photo);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.add_task_round));
                        //d.set
                        addPhoto.setHint("");
                        addPhoto.setCompoundDrawables(null,null,null,null);
                        addPhoto.setBackground(d);

                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        addPhoto.setBackground(d);
                    }

                    //photo.requestLayout();
                    //photo.getLayoutParams().height = 75;
                    //photo.getLayoutParams().width = 75;
                    //photo.setScaleType(ImageView.ScaleType.FIT_XY);
                    //photo.setVisibility(View.VISIBLE);
                    break;
            }

    }

}

//TODO: rounded corners at the photo
//TODO: stretched photo
//TODO: toolbar items
// todo: better EditTexts