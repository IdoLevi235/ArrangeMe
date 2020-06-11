package com.example.arrangeme.menu.schedule;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.Anchors.AnchorPagePopup;
import com.example.arrangeme.BuildSchedule.ChooseTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.menu.Homepage;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.tasks.TaskPagePopup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private TextView rateMsg;
    private Button chooseTaskBtn;
    private Button questionnaireBtn;
    private ProgressBar spinner;
    private RecyclerView recyclerSchedule;
    private AppCompatImageView rankit;
    private LinearLayoutManager layoutManager;
    private String[] myDataset;
    String deletedKey;
    String deletedCategory;
    String deletedDescription;
    String deletedLocation;
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
    private String deletedType;
    private String deletedActiveKey;
    private String deletedCreateDate;
    private String deletedDate;
    private String deletedEndTime;
    private String deletedStartTime;
    private Object deletedPhotoURI;
    private String deletedReminderType;
    private boolean isFromChooseTasks=false;

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

        // First entrance - take the date from homepage or put today's date
        date = ((Homepage) getActivity()).getDateToShowInScheduleFragment();
        if (date!=null) { // came from choosetasks
            ((Homepage) getActivity()).setDateToShowInScheduleFragment(null);
            spinner.setVisibility(View.VISIBLE);
            isFromChooseTasks=true;
        }
        else { //didn't come from choose tasks, show today's date
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        }
        datePicker.setText(date);
        datePicker.setOnClickListener(this);
        checkIfQuestionnaireFilled();
        initSwipes();
    }

    private void initSwipes() {
        try{
            ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID)
                        .child("Schedules").child(date).child("schedule");
                switch (direction) {
                    case ItemTouchHelper.LEFT :
                        deletedKey = fbAdapter.getRef(position).getKey();
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                deletedType = (String) dataSnapshot.child(deletedKey).child("type").getValue();
                                deletedCategory = (String) dataSnapshot.child(deletedKey).child("category").getValue();
                                deletedDescription = (String) dataSnapshot.child(deletedKey).child("description").getValue();
                                deletedLocation = (String) dataSnapshot.child(deletedKey).child("location").getValue();
                                deletedActiveKey = (String) dataSnapshot.child(deletedKey).child("activeKey").getValue();
                                deletedCreateDate = (String) dataSnapshot.child(deletedKey).child("createDate").getValue();
                                deletedDate = (String) dataSnapshot.child(deletedKey).child("date").getValue();
                                deletedEndTime = (String) dataSnapshot.child(deletedKey).child("endTime").getValue();
                                deletedStartTime = (String) dataSnapshot.child(deletedKey).child("startTime").getValue();
                                deletedPhotoURI = (String) dataSnapshot.child(deletedKey).child("photoUri").getValue();
                                deletedReminderType = (String) dataSnapshot.child(deletedKey).child("reminderType").getValue();

                                if (deletedType.equals("anchor")) {
                                    Toast.makeText(getContext(), "You can't delete an anchor!", Toast.LENGTH_LONG).show();
                                    fbAdapter.notifyDataSetChanged();
                                } else {
                                    fbAdapter.getRef(position).setValue(null);
                                    Snackbar.make(recyclerSchedule, "You deleted a " + deletedCategory
                                            + " task: " + deletedDescription, Snackbar.LENGTH_LONG)
                                            .setAction("Undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDatabase.child(deletedKey).child("category").setValue(deletedCategory);
                                                    mDatabase.child(deletedKey).child("description").setValue(deletedDescription);
                                                    mDatabase.child(deletedKey).child("location").setValue(deletedLocation);
                                                    mDatabase.child(deletedKey).child("type").setValue(deletedType);
                                                    mDatabase.child(deletedKey).child("startTime").setValue(deletedStartTime);
                                                    mDatabase.child(deletedKey).child("endTime").setValue(deletedEndTime);
                                                    mDatabase.child(deletedKey).child("date").setValue(deletedDate);
                                                    mDatabase.child(deletedKey).child("createDate").setValue(deletedCreateDate);
                                                    mDatabase.child(deletedKey).child("activeKey").setValue(deletedActiveKey);
                                                    mDatabase.child(deletedKey).child("photoUri").setValue(deletedPhotoURI);
                                                    mDatabase.child(deletedKey).child("reminderType").setValue(deletedReminderType);
                                                }
                                            }).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        break;
                    default:
                        break;
                    }

                }
            });
        helper.attachToRecyclerView(recyclerSchedule);

    } catch (Exception e) {
            e.printStackTrace();
        }
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
                    chooseMessage.setVisibility(View.GONE);
                    chooseTaskBtn.setVisibility(View.GONE);
                    rankit.setVisibility(View.GONE);
                    noSchRel.setVisibility(View.VISIBLE);
                  //  view4.setVisibility(View.GONE);
                    schExistRel.setVisibility(View.GONE);
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("You didn't fill the questionnaire yet.");
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
                else { // ques is ok
                    initializeSchedule();
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
        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        if(isFromChooseTasks==false) { // COMING FROM TAB SCHEDULE
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        rankit.setVisibility(View.GONE);
                        noSchRel.setVisibility(View.VISIBLE);
                   //     view4.setVisibility(View.GONE);
                        noScheduleYet.setVisibility(View.VISIBLE);
                        noScheduleYet.setText("You don't have schedule for this day!");
                        schExistRel.setVisibility(View.GONE);
                        chooseMessage.setVisibility(View.VISIBLE);
                        chooseTaskBtn.setVisibility(View.VISIBLE);
                        chooseTaskBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), ChooseTasks.class);
                                i.putExtra("date", date);
                                startActivity(i);
                            }
                        });
                    } else {
                        noSchRel.setVisibility(View.GONE);
                       // view4.setVisibility(View.VISIBLE);
                        schExistRel.setVisibility(View.VISIBLE);
                        initializeFirebaseUI();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (isFromChooseTasks==true){ // COMING FROM CHOOSE TASKS
            initializeFirebaseUI();
        }
    }

    private void showScheduleRate() {
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(date).child("data");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String rate = (String) dataSnapshot.child("successful").getValue();
                    if (rate.equals("yes")) {
                        rateMsg.setText("You rated this schedule as successful");
                    } else if (rate.equals("no")) {
                        rateMsg.setText("You rated this schedule as unsuccessful");
                    } else { // n/a
                        rateMsg.setText("");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeFirebaseUI() {
        showScheduleRate();
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase, MainModelSchedule.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                InitItemOfSchedule(holder, position, model); // Init each item in schedule
                setClickListenerToItem(holder, position,model); // Short click --> cancel pick
                setLongClickListenerToItem(holder, position); // Long clicks
                spinner.setVisibility(View.GONE);
                isFromChooseTasks=false;
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule, parent, false);
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
        rateMsg = view.findViewById(R.id.rateMsgSch);
        rankit = view.findViewById(R.id.rankit);
        rankit.setOnClickListener(this);
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
                        else {
                            Toast.makeText(getContext(),"You can't change anchor's hours",Toast.LENGTH_LONG).show();
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
                // Actions to do after 0.85 seconds

                String firstKey = String.valueOf(longPressKeys[0]);
                String secondKey = String.valueOf(longPressKeys[1]);

                String firstTaskCategory = (String) dataSnapshot.child(firstKey).child("category").getValue();
                String firstTaskDescription = (String) dataSnapshot.child(firstKey).child("description").getValue();
                String firstTaskLocation = (String) dataSnapshot.child(firstKey).child("location").getValue();
                String firstActiveKey = (String) dataSnapshot.child(firstKey).child("activeKey").getValue();

                String secondTaskCategory = (String) dataSnapshot.child(secondKey).child("category").getValue();
                String secondTaskDescription = (String) dataSnapshot.child(secondKey).child("description").getValue();
                String secondTaskLocation = (String) dataSnapshot.child(secondKey).child("location").getValue();
                String secondActiveKey = (String) dataSnapshot.child(secondKey).child("activeKey").getValue();

                dataSnapshot.child(firstKey).child("category").getRef().setValue(secondTaskCategory);
                dataSnapshot.child(firstKey).child("description").getRef().setValue(secondTaskDescription);
                dataSnapshot.child(firstKey).child("location").getRef().setValue(secondTaskLocation);
                dataSnapshot.child(firstKey).child("activeKey").getRef().setValue(secondActiveKey);

                dataSnapshot.child(secondKey).child("category").getRef().setValue(firstTaskCategory);
                dataSnapshot.child(secondKey).child("description").getRef().setValue(firstTaskDescription);
                dataSnapshot.child(secondKey).child("location").getRef().setValue(firstTaskLocation);
                dataSnapshot.child(secondKey).child("activeKey").getRef().setValue(firstActiveKey);

                fbAdapter.notifyItemChanged(longPressPositions[0], longPressPositions[1]);
                Snackbar.make(recyclerSchedule,"You swapped between " + firstTaskDescription
                        +" and " + secondTaskDescription ,Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataSnapshot.child(firstKey).child("category").getRef().setValue(firstTaskCategory);
                                dataSnapshot.child(firstKey).child("description").getRef().setValue(firstTaskDescription);
                                dataSnapshot.child(firstKey).child("location").getRef().setValue(firstTaskLocation);
                                dataSnapshot.child(firstKey).child("activeKey").getRef().setValue(firstActiveKey);

                                dataSnapshot.child(secondKey).child("category").getRef().setValue(secondTaskCategory);
                                dataSnapshot.child(secondKey).child("description").getRef().setValue(secondTaskDescription);
                                dataSnapshot.child(secondKey).child("location").getRef().setValue(secondTaskLocation);
                                dataSnapshot.child(secondKey).child("activeKey").getRef().setValue(secondActiveKey);

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

    private void setClickListenerToItem(MyViewHolder holder, int position, MainModelSchedule model) {
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
                            else { // 0 long presses untill now, show popup
                                Log.d("popopo", "onClick: start");
                                String id = model.getActiveKey();
                                Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("TaskKeyFromWeek", id);
                                b.putString("date",date);
                                if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                    b.putString("photo", "yes");
                                }
                                else {
                                    b.putString("photo", "no");
                                }
                                intent.putExtras(b);
                                Log.d("popopo", "onClick: activeKey=" + id);
                                startActivity(intent);
                            }
                        }

                        else if(dataSnapshot.child(key).child("type").getValue().equals("anchor") ){
                            String id = model.getAnchorID();
                            Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("AnchorKeyFromWeek", id);
                            if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                b.putString("photo", "yes");
                            }
                            else {
                                b.putString("photo", "no");
                            }
                            b.putString("date",date);
                            intent.putExtras(b);
                            startActivity(intent);
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
            holder.timeText.setText(model.getStartTime() + "-" + model.getEndTime());
            String cat = model.getCategory();
            SpannableStringBuilder str = null;
            if(cat!=null) {
                 str = new SpannableStringBuilder
                        (model.getDescription() + "\n\nCategory : " + cat);
            }
            else {
                str = new SpannableStringBuilder(model.getDescription());
            }
            str.setSpan(new RelativeSizeSpan(1.3f), 0, model.getDescription().length(), 0);
            str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.button.setText(str);
          //  holder.button.setLayoutParams(new LinearLayout.LayoutParams(600, 200));
          //  holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
         //   holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
            if (model.getType().equals("anchor")) {
                holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                holder.button.setBackgroundResource
                        (R.drawable.rounded_temp_grey_anchor);
            } else if (model.getType().equals("task")) {
                holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                holder.button.setBackgroundResource
                        (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())],
                                0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseDate2:
                createDatePickerDialog();
                break;
            case R.id.rankit:
                Bundle b = new Bundle();
                Intent intent = new Intent(getActivity(), ScheduleFeedback.class);
                b.putString("date",date);
                b.putBoolean("isFromScheduleTab",true);
                intent.putExtras(b);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            default:
                break;
        }
    }

    private void createDatePickerDialog() {
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
        dpd.show();

    }

}

