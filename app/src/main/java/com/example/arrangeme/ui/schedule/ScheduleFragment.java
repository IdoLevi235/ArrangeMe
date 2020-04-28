package com.example.arrangeme.ui.schedule;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ScheduleFragment<RecyclerAdapter> extends Fragment {
    private ScheduleViewModel scheduleViewModel;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView chooseMessage;
    private Button chooseTaskBtn;
    private Button questionnaireBtn;
    private RecyclerView recyclerSchedule;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private ArrayList<MainModelSchedule> mainModels;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelSchedule> options;
    private FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder> fbAdapter;
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        questionnaireBtn =  view.findViewById(R.id.questionnaireBtn);
        chooseTaskBtn = view.findViewById(R.id.chooseTaskBtn);
        chooseMessage = view.findViewById(R.id.chooseMessage);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet= view.findViewById(R.id.quesMessage);
        recyclerSchedule= view.findViewById(R.id.recyclerSchedule);
        recyclerSchedule.setHasFixedSize(true);
        //TODO: function that checks if there is a schedule, it means if the user chose tasks for today & fill the questionnaire(personality vector is ful), if not, visible the texts that I did.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        final int[] longPressCount = new int[1];
        final int[] longPressKeys = new int[2];
        final int[] longPressPositions = new int[2];

        longPressKeys[0]=-1;longPressKeys[1]=-1;
        longPressPositions[0]=-1;longPressPositions[1]=-1;

        Integer[] catIcon = {R.drawable.study_white,
                R.drawable.sport_white,
                R.drawable.work_white,
                 R.drawable.nutrition_white,
                R.drawable.family_white_frame,
                R.drawable.chores_white,
                R.drawable.relax_white,
                R.drawable.friends_white, 0};

        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport,
                R.drawable.category_btn_work,R.drawable.category_btn_friends, R.drawable.category_btn_nutrition,
                R.drawable.category_btn_family, R.drawable.category_btn_chores,
                R.drawable.category_btn_relax, R.drawable.category_btn_other};
        ///////////////////////////////////////////////////////////////////////////////////////////////
        Integer[] catBackgroundFull = //IMPORTANT: DONT CHANGE THE ORDER HERE!!!!
                {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                        R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                        R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                        R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_other_nostroke};
        ///////////////////////////////////////////////////////////////////////////////////////////////
        Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
                R.color.family,R.color.friends, R.color.chores, R.color.relax, R.color.other};
        String[] catName = {"Study", "Sport", "Work","Friends","Nutrition", "Family", "Chores", "Relax", "Other"};



        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase,MainModelSchedule.class).build();
        fbAdapter=new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {




            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                holder.timeText.setText(" "+model.getTime());
               holder.button.setText("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
                SpannableStringBuilder str = new SpannableStringBuilder
                        ("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
                str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, model.getCategory().length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new RelativeSizeSpan(1.05f), 0, model.getCategory().length()+1, 0);
                holder.button.setText(str);

                holder.button.setLayoutParams (new LinearLayout.LayoutParams(720, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));
                if(model.getType().equals("ANCHOR")) {
                    holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                }
                else if (model.getType().equals("TASK"))
                    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                holder.button.setBackgroundResource
                        (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds
                        (0,0,catIcon[TaskCategory.fromStringToInt(model.getCategory())],0);

                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = fbAdapter.getRef(position).getKey();
                                int x = Integer.parseInt(key);
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                holder.button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = fbAdapter.getRef(position).getKey();
                                int x = Integer.parseInt(key);
                                if (dataSnapshot.child(key).child("type").getValue().equals("TASK")) {
                                    if (longPressCount[0] == 2){ // 2 long presses already

                                    }
                                    else if (longPressCount[0] == 0) {//0 long presses until now
                                        longPressCount[0]++; //count=1;
                                        if (longPressKeys[0]==-1)
                                            longPressKeys[0]=Integer.parseInt(key);//get first key
                                        else longPressKeys[1]=Integer.parseInt(key);

                                        if (longPressPositions[0]==-1)
                                            longPressPositions[0]=(position);//get first key
                                        else longPressPositions[1]=(position);
                                        holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);

                                    }
                                    else if (longPressCount[0] == 1) {//1 long presses until now
                                        if (longPressKeys[0] == x || longPressKeys[1] == x) { // press cancel
                                            longPressKeys[0]=-1;
                                            String cat = (String) dataSnapshot.child(key).child("category").getValue();
                                            holder.button.setBackgroundResource(catBackgroundFull[TaskCategory.fromStringToInt(cat)]);
                                            longPressPositions[0]=-1;
                                            longPressCount[0]--;
                                        } else {
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

                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    // Actions to do after 5 seconds
                                                    Log.d("TAG", "onLongClick: positions = " + longPressPositions[0] + " " + longPressPositions[1]);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.DayBtn):

                break;
            case (R.id.WeekBtn):

                break;
            case (R.id.MonthBtn):

                break;
            default:
                break;
        }
    }

}


//TODO: while schedule is loading put something that spins with "loading schedule...."
//TODO: category text in the button will be bold, description will not be bold