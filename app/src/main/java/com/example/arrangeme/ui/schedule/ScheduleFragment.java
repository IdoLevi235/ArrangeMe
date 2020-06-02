package com.example.arrangeme.ui.schedule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.ChooseTasks.ChooseTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.Login;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleFragment<RecyclerAdapter> extends Fragment implements View.OnClickListener{
    private ScheduleViewModel scheduleViewModel;
    private RelativeLayout noSchRel;
    private RelativeLayout schExistRel;
    private View view4;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView chooseMessage;
    private Button chooseTaskBtn;
    private Button questionnaireBtn;
    private ProgressBar spinner;
    private RecyclerView recyclerSchedule;
    private LinearLayoutManager layoutManager;
    private String[] myDataset;
    String date;
    FirebaseUser user;
    String UID;
    private ArrayList<MainModelSchedule> mainModels;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelSchedule> options;
    private FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder> fbAdapter;
    final int[] longPressCount = new int[1];
    final int[] longPressKeys = new int[2];
    final int[] longPressPositions = new int[2];
    private Button datePicker;
    Integer[] catIcon = {R.drawable.study_white,
            R.drawable.sport_white,
            R.drawable.work_white,
            R.drawable.nutrition_white,
            R.drawable.family_white,
            R.drawable.chores_white,
            R.drawable.relax_white,
            R.drawable.friends_white, 0};
    Integer[] catBackgroundFull = //IMPORTANT: DONT CHANGE THE ORDER HERE!!!!
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_other_nostroke};
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeFields();
        initializeComponents(view);

        // First entrace - take the date from homepage or put today's date
        date = ((Homepage) getActivity()).getDateToShowInScheduleFragment();
        if (date!=null) { // came from choosetasks
            ((Homepage) getActivity()).setDateToShowInScheduleFragment(null);
        }
        else { //didn't come from choose tasks, show today's date
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        }
        datePicker.setText(date);
        datePicker.setOnClickListener(this);
        checkIfQuestionnaireFilled();
        initializeSchedule();
    }

    private void checkIfQuestionnaireFilled() {
        final ArrayList<Integer> q_answers = new ArrayList<Integer>() ;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference currUserRef = mDatabase.child("users").child(UID).child("personality_vector");
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
                if (q_answers.contains(0)) {
                    noSchRel.setVisibility(View.VISIBLE);
                    view4.setVisibility(View.GONE);
                    schExistRel.setVisibility(View.GONE);
                    quesMessage.setVisibility(View.VISIBLE);
                    questionnaireBtn.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for this day!");
                    int answers[] = new int[25];
                    int i=0;
                    for (Integer x : q_answers){
                        answers[i++]=x;
                    }
                    Intent intent = new Intent(getActivity(),Questionnaire.class);
                    intent.putExtra("answers",answers);
                    questionnaireBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeSchedule() {
        // set date
        /*Fire base UI stuff */
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID)
                .child("Schedules").child(date).child("schedule");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    noSchRel.setVisibility(View.VISIBLE);
                    view4.setVisibility(View.GONE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for this day!");
                    schExistRel.setVisibility(View.GONE);
                    chooseMessage.setVisibility(View.VISIBLE);
                    chooseTaskBtn.setVisibility(View.VISIBLE);
                    chooseTaskBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), ChooseTasks.class);
                            i.putExtra("date",date);
                            startActivity(i);
                        }
                    });
                }
                else {
                    noSchRel.setVisibility(View.GONE);
                    view4.setVisibility(View.VISIBLE);
                    schExistRel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase,MainModelSchedule.class).build();
        fbAdapter=new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                InitItemOfSchedule(holder,position,model); // Init each item in schedule
                setClickListenerToItem(holder,position); // Short click --> cancel pick
                setLongClickListenerToItem(holder,position); // Long clicks
                spinner.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule,parent,false);
                return new MyViewHolder(v);
            }
        };

        fbAdapter.startListening();
        recyclerSchedule.setAdapter(fbAdapter);

    }

    private void initializeComponents(View view) {
        datePicker = (Button)view.findViewById(R.id.chooseDate2);
        spinner=(ProgressBar)view.findViewById(R.id.progressBar2);
        spinner.setVisibility(View.VISIBLE);
        questionnaireBtn =  view.findViewById(R.id.questionnaireBtn);
        chooseTaskBtn = view.findViewById(R.id.chooseTaskBtn);
        chooseMessage = view.findViewById(R.id.chooseMessage);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet= view.findViewById(R.id.noScheduleYet);
        recyclerSchedule= view.findViewById(R.id.recyclerSchedule);
        recyclerSchedule.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        noSchRel=view.findViewById(R.id.noScheduleLayout);
        schExistRel=view.findViewById(R.id.scheduleExistsLayout);
        view4 = view.findViewById(R.id.view4);
    }

    private void initializeFields() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        longPressKeys[0]=-1;longPressKeys[1]=-1;
        longPressPositions[0]=-1;longPressPositions[1]=-1;
    }

    private void setLongClickListenerToItem(MyViewHolder holder, int position) {
        holder.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = fbAdapter.getRef(position).getKey();
                        int x = Integer.parseInt(key);
                        if (dataSnapshot.child(key).child("type").getValue().equals("task")) {
                            if (longPressCount[0] == 2){ // 2 long presses already
                                //nothing to do here yet..
                            }
                            else if (longPressCount[0] == 0) {//0 long presses until now
                                zeroLongPresses(holder,key,position);
                            }
                            else if (longPressCount[0] == 1) {//1 long presses until now
                                if (longPressKeys[0] == x || longPressKeys[1] == x) { // press cancel
                                    cancelPickedItemWithLongClick(holder,key,dataSnapshot);
                                } else { //this is the second pick --> make swap
                                    Handler handler = new Handler();
                                    longPressCount[0]++;//count=2;
                                    if (longPressKeys[0] == -1)
                                        longPressKeys[0] = Integer.parseInt(key);//get first key
                                    else longPressKeys[1] = Integer.parseInt(key);

                                    if (longPressPositions[0] == -1)
                                        longPressPositions[0] = position;//get first key
                                    else longPressPositions[1] = position;
                                    holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
                                    //SWAPPING HERE
                                    swapItems(dataSnapshot,handler);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        });
    }

    private void swapItems(DataSnapshot dataSnapshot, Handler handler) {
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds

                String firstKey = String.valueOf(longPressKeys[0]);
                String secondKey = String.valueOf(longPressKeys[1]);
                String firstTaskCategory = (String) dataSnapshot.child(firstKey).child("category").getValue();
                String firstTaskDescription = (String) dataSnapshot.child(firstKey).child("description").getValue();
                String firstTaskLocation = (String) dataSnapshot.child(firstKey).child("location").getValue();
                String secondTaskCategory = (String) dataSnapshot.child(secondKey).child("category").getValue();
                String secondTaskDescription = (String) dataSnapshot.child(secondKey).child("description").getValue();
                String secondTaskLocation = (String) dataSnapshot.child(secondKey).child("location").getValue();

                dataSnapshot.child(firstKey).child("category").getRef().setValue(secondTaskCategory);
                dataSnapshot.child(firstKey).child("description").getRef().setValue(secondTaskDescription);
                dataSnapshot.child(firstKey).child("location").getRef().setValue(secondTaskLocation);

                dataSnapshot.child(secondKey).child("category").getRef().setValue(firstTaskCategory);
                dataSnapshot.child(secondKey).child("description").getRef().setValue(firstTaskDescription);
                dataSnapshot.child(secondKey).child("location").getRef().setValue(firstTaskLocation);

                fbAdapter.notifyItemChanged(longPressPositions[0], longPressPositions[1]);
                Snackbar.make(recyclerSchedule,"You swapped between " + firstTaskDescription
                        +" and " + secondTaskDescription ,Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataSnapshot.child(firstKey).child("category").getRef().setValue(firstTaskCategory);
                                dataSnapshot.child(firstKey).child("description").getRef().setValue(firstTaskDescription);
                                dataSnapshot.child(firstKey).child("location").getRef().setValue(firstTaskLocation);

                                dataSnapshot.child(secondKey).child("category").getRef().setValue(secondTaskCategory);
                                dataSnapshot.child(secondKey).child("description").getRef().setValue(secondTaskDescription);
                                dataSnapshot.child(secondKey).child("location").getRef().setValue(secondTaskLocation);
                            }
                        }).show();
                longPressCount[0]=0;
                longPressKeys[0]=-1;longPressKeys[1]=-1;
                longPressPositions[0]=-1;longPressPositions[1]=-1;



            }
        }, 850);

    }

    private void cancelPickedItemWithLongClick(MyViewHolder holder, String key, DataSnapshot dataSnapshot) {
        longPressKeys[0]=-1;
        String cat = (String) dataSnapshot.child(key).child("category").getValue();
        holder.button.setBackgroundResource(catBackgroundFull[TaskCategory.fromStringToInt(cat)]);
        longPressPositions[0]=-1;
        longPressCount[0]--;
    }

    private void zeroLongPresses(MyViewHolder holder, String key, int position) {
        longPressCount[0]++; //count=1;
        if (longPressKeys[0]==-1)
            longPressKeys[0]=Integer.parseInt(key);//get first key
        else longPressKeys[1]=Integer.parseInt(key);

        if (longPressPositions[0]==-1)
            longPressPositions[0]=(position);//get first key
        else longPressPositions[1]=(position);
        holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
    }

    private void setClickListenerToItem(MyViewHolder holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = fbAdapter.getRef(position).getKey();
                        int x = Integer.parseInt(key);
                        if (dataSnapshot.child(key).child("type").getValue().equals("task")) {
                            if (longPressCount[0] == 1) {//1 long presses until now
                                if (longPressKeys[0] == x || longPressKeys[1] == x) { // press cancel
                                    longPressKeys[0] = -1;
                                    String cat = (String) dataSnapshot.child(key).child("category").getValue();
                                    holder.button.setBackgroundResource(catBackgroundFull[TaskCategory.fromStringToInt(cat)]);
                                    longPressPositions[0] = -1;
                                    longPressCount[0]--;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void InitItemOfSchedule(MyViewHolder holder, int position, MainModelSchedule model) {
        try {
            holder.timeText.setText(model.getStartTime() + " - " + model.getEndTime());
            //holder.button.setText("\t"+model.getDescription()+" \n\n\t"+"Category: " + model.getCategory().toLowerCase());
            SpannableStringBuilder str = new SpannableStringBuilder
                (model.getDescription() + "\n\nCategory : " + model.getCategory());
            str.setSpan(new RelativeSizeSpan(1.3f), 0, model.getDescription().length() + 1, 0);
            str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.button.setText(str);
            holder.button.setLayoutParams(new LinearLayout.LayoutParams(700, 200));
            holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
            if (model.getType().equals("anchor")) {
                holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                holder.button.setBackgroundResource
                        (R.drawable.rounded_temp_grey_anchor);
            } else if (model.getType().equals("task")) {
                holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                holder.button.setBackgroundResource
                        (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())], 0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseDate2:
                DatePickerDialog datePickerDialog = createDatePickerDialog();
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }

    private DatePickerDialog createDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // these two lines in order to make it 01-05-2020 instead of 1-5-2020
                String day = dayOfMonth<10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String month = monthOfYear+1<10 ? "0" + String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
                date = day+"-"+(month)+"-"+year;
                datePicker.setText(date);
                initializeSchedule();
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        return dpd;

    }

}

