package com.example.arrangeme.BuildSchedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.R;
import com.example.arrangeme.ReminderBroadcast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Choose tasks class - here the user chooses tasks from his pending tasks that he wants to do
 * in a specific day.
 */

public class ChooseTasks extends AppCompatActivity implements View.OnClickListener {
    private final int MORNING = 5; //morning = 06:00-11:00
    private final int NOON = 6; //noon = 11:00-17:00
    private final int EVENING = 4;//evening = 17:00-21:00
    private final int NIGHT = 3; // night = 21:00-24:00

    private FirebaseFunctions mFunctions;
    private Toolbar toolbar;
    private int numOfTasksToChoose; //needs to be recieved from DB
    private int count;
    private TextView numberTextView;
    private TextView howMuchMore;
    private Button confirm;
    private RecyclerView mRecycler;
    private RelativeLayout rl;
    private View view;
    private LinearLayoutManager layoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModel> options;
    private FirebaseRecyclerAdapter<MainModel, MyViewHolder> fbAdapter;
    private TextView tv;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private View view4;
    private ProgressBar spinner;
    private Button setDate;
    private ImageView imv4;
    private Button notif;
    private RelativeLayout noTasksLayout;
    Integer[] catIcon = {R.drawable.study, R.drawable.sport,
            R.drawable.work, R.drawable.nutrition,
            R.drawable.familycat, R.drawable.chores,
            R.drawable.relax, R.drawable.friends_cat, 0};
    ArrayList<String> categoriesChosen = new ArrayList<>();
    ArrayList<Integer> positionsMarked = new ArrayList<>();
    ArrayList<String> keysChosen = new ArrayList<>();
    Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
            R.color.family, R.color.chores, R.color.relax,R.color.friends, R.color.other};
    Integer[] catIconWhite = {R.drawable.study_white,
            R.drawable.sport_white,
            R.drawable.work_white,
            R.drawable.nutrition_white,
            R.drawable.family_white,
            R.drawable.chores_white,
            R.drawable.relax_white,
            R.drawable.friends_white, 0};
    Map<String, Boolean> hoursMap;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String UID;
    @Override

    /**
     * onCreate method - set the initial parameters to all of xml components in this page
     * @params savedInstanceState
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tasks);
        initializeComponents();
        initializeFields();
        initializeToolbar();

        // get extras (date)
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        Log.d("TAG3", "onCreate: " + date);
        if (date!=null) {
            setDate.setText(date);
        }
        else {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            setDate.setText(currentDate);
        }
        String d = setDate.getText().toString();
        Log.d("TAG2", "onCreate: " + d);
        checkNumberOfFreeHours(d);
        setRecycler(mRecycler);
    }

    /**
     * Initialize the toolbar
     */
    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_40);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Initialize all components from XML and setting texts, onclick listeners for them
     */
    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_chooseTasks);
        notif=findViewById(R.id.notif);
        notif.setOnClickListener(this);
        view4=findViewById(R.id.view4);
        imv4 = findViewById(R.id.imageView4);
        spinner = findViewById(R.id.progressBar3);
        spinner.setVisibility(View.VISIBLE);
        tv = findViewById(R.id.textView16);
        tv.setVisibility(View.GONE);
        noTasksLayout= findViewById(R.id.noTasksLayout);
        tv2=findViewById(R.id.textViewExplanation);
        tv3= findViewById(R.id.textViewPleaseChoose);
        tv4= findViewById(R.id.textView17);
        numberTextView = (TextView) findViewById(R.id.textViewNumbersRed);
        numberTextView.setText(Integer.toString(count));
        numberTextView.setBackgroundResource(R.drawable.green_textview);
        howMuchMore = (TextView) findViewById(R.id.textViewHowManyMore);
        confirm = (Button) findViewById(R.id.confirmTasksBtn);
        confirm.setOnClickListener(this);
        setDate = (Button)findViewById(R.id.chooseDate);
        setDate.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(ChooseTasks.this, LinearLayoutManager.VERTICAL, false);
        mRecycler = findViewById(R.id.recylcler_choosetasks);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);

    }

    /**
     * Initialize other fields of this class
     */
    private void initializeFields() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        mFunctions = FirebaseFunctions.getInstance();
        count = 0;
    }

    /**
     * @param date
     * check if there is already a schedule in the system for this user in this date.
     * if so - give him a special message
     */
    private void checkIfThereIsSchedule(String date) {
       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(date).child("schedule");
       mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount()>0){//There is already a schedule in this day
                String txt1="\nPay attention! You already have schedule for this day.";
                SpannableString txtSpannable= new SpannableString(txt1);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                txtSpannable.setSpan(boldSpan, 0, txt1.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, txt1.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv2.append(txtSpannable);
            }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }


    /**
     * set Recycler - initial setup of the recycler view
     * @param mRecyclerView
     *
     */
    private void setRecycler(RecyclerView mRecyclerView) {
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
        options = new FirebaseRecyclerOptions.Builder<MainModel>().setQuery(mDatabase, MainModel.class).build();
        checkIfThereArePendingTasks(mDatabase);
        /* Fire base UI stuff */
        fbAdapter = new FirebaseRecyclerAdapter<MainModel, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModel model) {
                if (position==0){
                    ViewGroup.LayoutParams params=mRecycler.getLayoutParams();
                    int x = fbAdapter.getItemCount();
                    x = x>2 ? 2 : x;
                    params.height=245*(x);
                    mRecycler.setLayoutParams(params);
                }
                SpannableStringBuilder str = null;
                    str = new SpannableStringBuilder
                            (model.getDescription() + "\n\nCategory : ");
                str.setSpan(new RelativeSizeSpan(1.5f), 0, model.getDescription().length(), 0);
                str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.button.setTypeface(Typeface.create("montserrat", Typeface.NORMAL));
                holder.button.setText(str);
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(800, 180));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                if (positionsMarked.contains(position)){ // to keep it marked when scrolling
                    holder.button.setBackgroundResource(R.drawable.rounded_dark_primary);
                    holder.button.setTextColor(Color.WHITE);
                    holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIconWhite[x], 0);
                }
                else {
                    holder.button.setTextColor(ContextCompat.getColor(ChooseTasks.this, catColor[TaskCategory.fromStringToInt(model.getCategory())]));
                    holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
                    holder.button.setBackgroundResource(R.drawable.category_btn_schedule);
                   // holder.button.setTextSize(12);
                }
                holder.button.setPadding(15,10,15,0);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        itemOnClick(holder, position, model);
                    }
                });
                spinner.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_tab, parent, false);
                return new MyViewHolder(v);
            }

        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);
        /* Fire base UI stuff End */

    }

    /**
     * itemOnClick - the click listenerfor each of the recycler view elements
     * @param holder
     * @param position
     * @param model
     *
     */
    private void itemOnClick(MyViewHolder holder, int position, MainModel model) {
        Drawable dDarkblue = ResourcesCompat.getDrawable(getResources(),
                R.drawable.rounded_dark_primary, null);
        Drawable dCurr = holder.button.getBackground();
        Drawable.ConstantState constantStateDrawableA = dDarkblue.getConstantState();
        Drawable.ConstantState constantStateDrawableB = dCurr.getConstantState();
        if (!constantStateDrawableA.equals(constantStateDrawableB)) { //Not pressed yet - pick
            String key = fbAdapter.getRef(position).getKey();
            keysChosen.add(key);
            chooseTask(holder,model);
            categoriesChosen.add(model.getCategory());
            positionsMarked.add(position);
            holder.button.setPadding(15,10,15,0);
        } else { //pressed, unpick
            String key = fbAdapter.getRef(position).getKey();
            keysChosen.remove(key);
            unChooseTask(holder, model);
            categoriesChosen.remove(model.getCategory());
            positionsMarked.remove((Integer)position);
        }
    }

    /**
     * unChooseTask - click listener in case  that the element is already picked
     * @param holder
     * @param model
     */
    private void unChooseTask(MyViewHolder holder, MainModel model) {
        int x = TaskCategory.fromStringToInt(model.getCategory());
        holder.button.setBackgroundResource(R.drawable.category_btn_schedule);
        holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
        holder.button.setTextColor(ContextCompat.getColor(ChooseTasks.this, catColor[TaskCategory.fromStringToInt(model.getCategory())]));
        count--;
        if (count == numOfTasksToChoose - 1) { //green-->red
            numberTextView.setBackgroundResource(R.drawable.green_textview);
        }
        numberTextView.setText(Integer.toString(count));
        howMuchMore.setText("(You can choose " + Integer.toString(numOfTasksToChoose - count) + " more tasks..)");
    }

    /**
     * chooseTask - click listener in case that the element haven't been picked yet
     * @param holder
     * @param model
     */
    private void chooseTask(MyViewHolder holder, MainModel model) {
        int x = TaskCategory.fromStringToInt(model.getCategory());
        if (count < numOfTasksToChoose) { //stay red
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_dark_primary);
            holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIconWhite[x], 0);
            holder.button.setTextColor(Color.WHITE);
            numberTextView.setBackgroundResource(R.drawable.green_textview);
            numberTextView.setText(Integer.toString(count));
            howMuchMore.setText("(You can choose " + (numOfTasksToChoose - count) + " more tasks..)");

        }
        else if (count == numOfTasksToChoose - 1)
        { //red--->green
            Log.d("TAG6", "chooseTask: green");
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_rec_blue_nostroke);
            numberTextView.setBackgroundResource(R.drawable.red_textview);
            numberTextView.setText(Integer.toString(count));
            howMuchMore.setText("(You can choose " + (numOfTasksToChoose - count) + " more tasks..)");
        }
        else if (numOfTasksToChoose == count) { //more than numOfTasks, uncheck the last one checked
            Toast.makeText(getApplicationContext(), "You can choose max " + numOfTasksToChoose + " tasks !", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * checkIfThereArePendingTasks - funtion that checks if the user has any pending tasks to show in the recyclerview
     * @param mDatabase
     */
    private void checkIfThereArePendingTasks(DatabaseReference mDatabase) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    tv.setText("You have no pending tasks");
                    tv4.setText(" You can add new tasks by pressing the "+'"'+"Add New Task"+'"'+" button below");
                    noTasksLayout.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.GONE);
                    tv3.setVisibility(View.INVISIBLE);
                    setDate.setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    howMuchMore.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                  //  view4.setVisibility(View.GONE);
                    imv4.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    numberTextView.setVisibility(View.GONE);
                    confirm.setText("Add new task");
                }

                else {
                    tv.setVisibility(View.GONE);
                    howMuchMore.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    //view4.setVisibility(View.VISIBLE);
                    imv4.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    numberTextView.setVisibility(View.VISIBLE);
                    confirm.setText("Build Your Schedule");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * onClick listener
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTasksBtn:
              if (setDate.getText().length()==0){ // choose tasks validation
                  String str = "You must choose a date!";
                  chooseTaskFailed(str);
              }
              //check that he chose at least one task
              else if (categoriesChosen.isEmpty() && confirm.getText().toString().equals("Build Your Schedule")) {
              String str = "You must choose at least one task!";
              chooseTaskFailed(str);
             }

              else { // validations passed
                    String s = confirm.getText().toString() ;
                    Log.d("TAG9", "onClick: " + s);
                    if (s.equals("Add new task")){ // add new tasks button
                        Intent i = new Intent(ChooseTasks.this,AddTasks.class);
                        startActivity(i);
                    }
                    else { // build schedule button
                        isPastSchedulesRated();
                    }
              }
                break;

            case R.id.chooseDate:
                DatePickerDialog datePickerDialog = createDatePickerDialog();
                datePickerDialog.show();
                break;
            case R.id.notif:
                // Notifications stuff //
                Log.d("TAG7", "onClick: notif pressed");
                String cid = "notifyQues";
                CharSequence name = "QuestionnaireNotfilledNotification";
                String description = "Channel to notify on unfilled questionnaires";
                createNotificationChannel(cid,name,description);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,13);
                calendar.set(Calendar.MINUTE,9);
                Intent ct1 = new Intent(getApplicationContext(), ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,ct1,0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                // Notifications stuff end //


            default:
                break;
        }//end of switch

    } //end of onclick

    /**
     * Before building a schedule, we check if the user rated all of his past schedule (before today)
     * if he did - we build a schedule
     * if not - we direct him to schedule tab to the specific unrated schedule
     */
    private void isPastSchedulesRated() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules");
        ArrayList<LocalDate> datesWithUnratedSchedule = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date_str="";
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("data").child("successful").getValue().equals("n/a")){
                        date_str =  ds.getKey();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        datesWithUnratedSchedule.add(LocalDate.parse(date_str,dateFormatter));
                    }
                }
                LocalDate today = LocalDate.now(); // get todays date
                boolean flag = false;
                for (LocalDate date : datesWithUnratedSchedule){
                    if (date.isBefore(today)){
                        showSchUnratedAlert(date_str);
                        flag=true;
                        break;
                    }
                }

                if (flag==false) { // there are no un-rated past schedule, so its OK, go and build schedule
                    String date = (String) setDate.getText();
                    chooseTaskSuccess(date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * @param date
     * Alert if there are past unrated schedules
     */
    private void showSchUnratedAlert(String date) {
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog(ChooseTasks.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sorry!")
                .setContentText(("The system cannot create a schedule for you yet.\nYou didn't rate " +
                                "the schedule from " + date + ".\n" +
                        "Your review on each past schedule is very important, in order to create the best schedule for you."));
        ad.setConfirmText("Rate now");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Bundle b = new Bundle();
                Intent intent = new Intent(ChooseTasks.this, Homepage.class);
                b.putString("FromHomepage","3");
                b.putString("date", date.toString());
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);

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
        channel.setName(name);
        channel.setShowBadge(true);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * this function adds frequency vector and time vector to the correct pace in DB
     * @param frequencyVector
     * @param timeVector
     * @param date
     */
    private void addVectorsToDB(Map<String, Integer> frequencyVector, Map<String, Integer> timeVector, String date) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules");
        mDatabase.child(date).child("data").child("frequency_vector").setValue(frequencyVector);
        mDatabase.child(date).child("data").child("time_vector").setValue(timeVector);
        mDatabase.child(date).child("data").child("successful").setValue("n/a");
}

    /**
     * this function calculates, creates and returns the time vector according to this user's
     * free time windows in a specific date chosen
     * @param hoursMap
     * @return time vector in Map object
     */
    private Map<String, Integer> createTimeVector(Map<String, Boolean> hoursMap) {
        Map<String,Integer> timeVector = new LinkedHashMap<>();

        int morning=0;
        int noon=0;
        int evening=0;
        int night=0;
        int index=0;
        for (String key:hoursMap.keySet()){
            if (index<MORNING*2){
                if (hoursMap.get(key)==false){
                    morning++;
                }
            }
            else if (MORNING*2<=index && index < (NOON*2+MORNING*2)){
                if (hoursMap.get(key)==false){
                    noon++;
                }
            }
            else if ((NOON*2+MORNING*2)<=index && index<(EVENING*2+NOON*2+MORNING*2)){
                if (hoursMap.get(key)==false){
                    evening++;
                }
            }
            else if ((EVENING*2+NOON*2+MORNING*2)<=index && index<=(EVENING*2+NOON*2+MORNING*2+NIGHT*2)){
                if (hoursMap.get(key)==false){
                    night++;
                }
            }
            index++;
        }
        timeVector.put("Morning",morning/2);
        timeVector.put("Noon",noon/2);
        timeVector.put("Evening",evening/2);
        timeVector.put("Night",night/2);
        return timeVector;
    }

    /**
     * this function calculates, creates and returns the frequency vector according to this user's chosen tasks
     * @param categoriesChosen
     * @return frequncy vector in Map object
     */
    private Map<String,Integer> createFreVec(ArrayList<String> categoriesChosen) {
        Map<String,Integer> freqVec = new LinkedHashMap<>();
        freqVec.put("Study", Collections.frequency(categoriesChosen,"STUDY"));
        freqVec.put("Sport", Collections.frequency(categoriesChosen,"SPORT"));
        freqVec.put("Work", Collections.frequency(categoriesChosen,"WORK"));
        freqVec.put("Nutrition", Collections.frequency(categoriesChosen,"NUTRITION"));
        freqVec.put("Family", Collections.frequency(categoriesChosen,"FAMILY"));
        freqVec.put("Chores", Collections.frequency(categoriesChosen,"CHORES"));
        freqVec.put("Relax", Collections.frequency(categoriesChosen,"RELAX"));
        freqVec.put("Friends", Collections.frequency(categoriesChosen,"FRIENDS"));
        freqVec.put("Other", Collections.frequency(categoriesChosen,"OTHER"));
        return freqVec;
    }


    /**
     * this function creates the date picker dialog
     * @return DatePickerDialog
     */
    private DatePickerDialog createDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // these two lines in order to make it 01-05-2020 instead of 1-5-2020
                String day = dayOfMonth<10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String month = monthOfYear+1<10 ? "0" + String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
                //

                String date = day+"-"+(month)+"-"+year;
                setDate.setText(date);
                tv2.setVisibility(View.VISIBLE);
                numberTextView.setText("0");
                checkNumberOfFreeHours(date);
                howMuchMore.setText("");
                categoriesChosen.clear();
                positionsMarked.clear();
                count=0;
                setRecycler(mRecycler);
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        return dpd;
    }

    /**
     * this functions recieves a date and checks how many free-time windows
     * the user has in this day. this is helpfull in creating the time vector and frequency vector
     * @param date_
     */
    private void checkNumberOfFreeHours(String date_) { // Time windows can be from 06:00 to 24:00
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
        Log.d("TAG6", "checkNumberOfFreeHours: date1=" + date_);

        //make the date dd-mm-yyyy

        String[] dateArr = date_.split("-");
        String y = dateArr[2];
        String m = dateArr[1];
        String d = dateArr[0];

        d = d.length()==1? "0"+d : d;
        m = m.length()==1? "0"+m : m;

        String date = d + '-' + m + '-' + y; // now date is in our DB format..

        Query q = mDatabase.orderByChild("date").equalTo(date);
        Log.d("TAG6", "checkNumberOfFreeHours: query = " + q);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag = false;
                int count;
                hoursMap = new LinkedHashMap<String, Boolean>()
                {{
                    put("06:00", false); put("06:30", false); put("07:00", false); put("07:30", false); put("08:00", false); put("08:30", false);
                    put("09:00", false); put("09:30", false); put("10:00", false); put("10:30", false); put("11:00", false); put("11:30", false);
                    put("12:00", false); put("12:30", false); put("13:00", false); put("13:30", false); put("14:00", false); put("14:30", false);
                    put("15:00", false); put("15:30", false); put("16:00", false); put("16:30", false); put("17:00", false); put("17:30", false);
                    put("18:00", false); put("18:30", false); put("19:00", false); put("19:30", false); put("20:00", false); put("20:30", false);
                    put("21:00", false); put("21:30", false); put("22:00", false); put("22:30", false); put("23:00", false); put("23:30", false);
                    put("00:00", false);
                }};
                ; // Key = hour, Value = busy/not
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("TAG6", "onDataChange: inside for" + ds);
                    String s = (String) ds.child("startTime").getValue();
                    String e = (String) ds.child("endTime").getValue();
                    List<String> keyList = new ArrayList<String>(hoursMap.keySet());
                    for (String key : keyList){
                        if (key.equals(s)){
                            flag=true;
                            hoursMap.replace(key,true);
                        }

                        else if (key.equals(e)){
                            flag=false;
                            hoursMap.replace(key,true);
                        }

                        else if (flag==true){
                            hoursMap.replace(key,true);
                        }

                    }
                }
                Log.d("TAG6", "onDataChange: " + hoursMap);
                count=0;
                    for (boolean value : hoursMap.values()){
                        if (value==false) count++; // count all free time
                    }
                    float numOfFreeHours = (count)/2;
                    tv2.setText("On "
                                +date+" you have " + numOfFreeHours+" free hours.\nYou can choose up to "
                                + (int)numOfFreeHours + " tasks to do in this day.");
                checkIfThereIsSchedule(date);
                numOfTasksToChoose=(int)numOfFreeHours;
            }

            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    /**
     * after submiting the tasks, this function gives a success alert box
     * @param date
     */
    private void chooseTaskSuccess(String date) {
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog( ChooseTasks.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText(("Are you sure you are done?\n (After pressing yes, the system will build" +
                        " a schedule for you for " + date + ")"));

        ad.setCancelText("No");
        ad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                keysChosen.clear();
            }
        });

        ad.setConfirmText("Yes");
        Intent intent = new Intent(ChooseTasks.this, Homepage.class);
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Map<String,Integer> frequencyVector = createFreVec(categoriesChosen);
                Log.d("TAG6", "onClick:freq= " + frequencyVector);
                Map<String, Integer> timeVector = createTimeVector(hoursMap);
                Log.d("TAG6", "onClick:time= " + timeVector);
                //Log.d("TAG9", "onClick: keys chosen = " + keysChosen); works

                addVectorsToDB(frequencyVector,timeVector,date);
                moveChosenTasksFromPendingTasks();
                buildSchedule(timeVector,frequencyVector,date);
                intent.putExtra("FromHomepage", "3");
                intent.putExtra("date",date);
                Log.d("TAG1", "onClick in choosetasks: " + date);
                startActivity(intent);
            }
        });

        ad.show();
        //Button btn = (Button) ad.findViewById(R.id.confirm_button);
        //btn.setBackgroundResource(R.drawable.rounded_rec);
        // Button btn1 = (Button) ad.findViewById(R.id.cancel);
    }

    /**
     * Remove chosen tasks from pending tasks into temp folder in DB
     */
    private void moveChosenTasksFromPendingTasks() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    if (keysChosen.contains(key)){
                        try {
                            //getting task details to preserve
                            String category = (String) ds.child("category").getValue();
                            String createDate = (String) ds.child("createDate").getValue();
                            String description = (String) ds.child("description").getValue();
                            String location = (String) ds.child("location").getValue();
                            String reminderType = (String) ds.child("reminderType").getValue();
                            String photoURI = (String) ds.child("photoUri").getValue();
                            ds.getRef().setValue(null);
                            moveTask(key,category,createDate,description,location,reminderType,photoURI);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /**
     * Remove a task from pending tasks into temp folder in DB
     */

    private void moveTask(String key, String category, String createDate, String description, String location, String reminderType, String photoURI) {
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("temp");
        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child(key).getRef().child("category").setValue(category);
                dataSnapshot.child(key).getRef().child("createDate").setValue(createDate);
                dataSnapshot.child(key).getRef().child("description").setValue(description);
                dataSnapshot.child(key).getRef().child("location").setValue(location);
                dataSnapshot.child(key).getRef().child("reminderType").setValue(reminderType);
                dataSnapshot.child(key).getRef().child("photoUri").setValue(photoURI);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * @param timeVector
     * @param frequencyVector
     * @param date
     * Schedule building proccess starts here.
     */
    public void buildSchedule(Map<String, Integer> timeVector, Map<String, Integer> frequencyVector,String date) {
        ArrayList<Integer> timeArray = new ArrayList<>();
        ArrayList<Integer> freqArray = new ArrayList<>();
        for (Integer x : timeVector.values())
            timeArray.add(x);
        for (Integer y : frequencyVector.values())
            freqArray.add(y);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personal_info");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long g = (Long) dataSnapshot.child("group").getValue();
                Long p = (Long) dataSnapshot.child("points").getValue(); // points
                Long newp = p+10;
                mDatabase.child("points").setValue(newp);
                if (newp<200) {
                    Globals.checkForNewLevel(mDatabase, newp);
                }
                Integer group = g.intValue();
                CreateSchedule ce = new CreateSchedule();
                Log.d("CreateSchedule", +group+timeArray.toString()+freqArray.toString());

                //check if user centered or not
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules");
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()<30){
                            ce.findBestSchedule(Math.toIntExact(group),timeArray,freqArray,date,keysChosen);
                        }
                        else {
                            ce.findScheduleUserCentered(timeArray,freqArray,date,keysChosen);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    /**
     * if there was a problem (not enough tasks picked for example)
     * this function will show error alertbox
     * @param str
     */

    private void chooseTaskFailed(String str) {
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog(ChooseTasks.this, SweetAlertDialog.WARNING_TYPE);
        ad.setTitleText("Error");
        ad.setContentText(str);
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);
    }
}

