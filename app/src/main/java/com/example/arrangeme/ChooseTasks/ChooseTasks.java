package com.example.arrangeme.ChooseTasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.text.SimpleDateFormat;
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
    private TextView helloTxt;
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
    private View view4;
    private ProgressBar spinner;
    private Button setDate;
    private ImageView imv4;
    Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,
            R.drawable.work_white, R.drawable.nutrition_white,
            R.drawable.family_white, R.drawable.chores_white,
            R.drawable.relax_white, R.drawable.friends_white, 0};
    Integer[] catBackgroundFull =
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke,
                    R.drawable.rounded_rec_other_nostroke};

    ArrayList<String> categoriesChosen = new ArrayList<>();
    ArrayList<Integer> positionsMarked = new ArrayList<>();
    Map<String, Boolean> hoursMap;
    @Override

    /**
     * onCreate method - set the initial parameters to all of xml components in this page
     * @params savedInstanceState
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tasks);
        mFunctions = FirebaseFunctions.getInstance();
        toolbar = findViewById(R.id.toolbar_chooseTasks);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.backsmall);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        view4=findViewById(R.id.view4);

        imv4 = findViewById(R.id.imageView4);

        spinner = findViewById(R.id.progressBar3);
        spinner.setVisibility(View.VISIBLE);

        tv = findViewById(R.id.textView16);
        tv.setVisibility(View.GONE);

        tv2=findViewById(R.id.textViewExplanation);
        //tv2.setVisibility(View.INVISIBLE);

        numberTextView = (TextView) findViewById(R.id.textViewNumbersRed);
        numberTextView.setText(Integer.toString(count));
        numberTextView.setBackgroundResource(R.drawable.red_textview);

        howMuchMore = (TextView) findViewById(R.id.textViewHowManyMore);
        //howMuchMore.setText("You can choose " + (numOfTasksToChoose - count) + " more tasks");

        helloTxt = (TextView) findViewById(R.id.textViewHello);
        helloTxt.setText("Hello, " + Globals.currentUsername + "!");

        count = 0;

        confirm = (Button) findViewById(R.id.confirmTasksBtn);
        confirm.setOnClickListener(this);

        setDate = (Button)findViewById(R.id.chooseDate);
        setDate.setOnClickListener(this);
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

        layoutManager = new LinearLayoutManager(ChooseTasks.this, LinearLayoutManager.VERTICAL, false);
        mRecycler = findViewById(R.id.recylcler_choosetasks);
        String d = setDate.getText().toString();
        Log.d("TAG2", "onCreate: " + d);
        checkNumberOfFreeHours(d);
        setRecycler(mRecycler);

    }

    private void checkIfThereIsSchedule(String date) {
       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(date);
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
                holder.button.setText("\t" + model.getCategory() + " \n\n\t" + model.getDescription());
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(800, 180));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                if (positionsMarked.contains(position)){
                    holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
                }
                else {
                    holder.button.setBackgroundResource(catBackgroundFull[x]);
                }
                holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
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
                R.drawable.rounded_rec_darkblue_nostroke, null);
        Drawable dCurr = holder.button.getBackground();
        Drawable.ConstantState constantStateDrawableA = dDarkblue.getConstantState();
        Drawable.ConstantState constantStateDrawableB = dCurr.getConstantState();
        if (!constantStateDrawableA.equals(constantStateDrawableB)) { //Not pressed yet
            chooseTask(holder);
            categoriesChosen.add(model.getCategory());
            positionsMarked.add(position);

        } else { //pressed, unpick
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
        holder.button.setBackgroundResource(catBackgroundFull[x]);
        count--;
        if (count == numOfTasksToChoose - 1) { //green-->red
            numberTextView.setBackgroundResource(R.drawable.red_textview);
        }
        numberTextView.setText(Integer.toString(count));
        howMuchMore.setText("(You can choose " + Integer.toString(numOfTasksToChoose - count) + " more tasks..)");
    }

    /**
     * chooseTask - click listener in case that the element haven't been picked yet
     * @param holder
     */
    private void chooseTask(MyViewHolder holder) {
        if (count < numOfTasksToChoose) { //stay red
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
            numberTextView.setBackgroundResource(R.drawable.red_textview);
            numberTextView.setText(Integer.toString(count));
            howMuchMore.setText("(You can choose " + (numOfTasksToChoose - count) + " more tasks..)");

        }
        else if (count == numOfTasksToChoose - 1)
        { //red--->green
            Log.d("TAG6", "chooseTask: green");
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
            numberTextView.setBackgroundResource(R.drawable.green_textview);
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
                    tv.setText("You have no pending tasks.\n You can add new tasks in tasks tab");
                    tv.setVisibility(View.VISIBLE);
                    howMuchMore.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    confirm.setEnabled(false);
                    view4.setVisibility(View.GONE);
                    imv4.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    numberTextView.setVisibility(View.GONE);
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
//                else if (count < numOfTasksToChoose) {
//                String str = "You must choose " + numOfTasksToChoose + " tasks!";
//                chooseTaskFailed(str);
//            }

                else {
                  String date = (String) setDate.getText();
                  chooseTaskSuccess(date);
              }
                break;

            case R.id.chooseDate:
                DatePickerDialog datePickerDialog = createDatePickerDialog();
                datePickerDialog.show();
                break;
            default:
                break;
        }//end of switch

    } //end of onclick

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
                addVectorsToDB(frequencyVector,timeVector,date);
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

