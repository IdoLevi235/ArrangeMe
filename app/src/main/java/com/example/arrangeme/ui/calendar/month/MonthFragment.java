package com.example.arrangeme.ui.calendar.month;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arrangeme.AddAnchor;
import com.example.arrangeme.AnchorPagePopup;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;


/**
 * @param <RecyclerAdapter>
 * This class controls the month view calendar
 */
public class MonthFragment<RecyclerAdapter> extends Fragment implements  View.OnClickListener {
    Integer[] catIcon = {R.drawable.study_white,
            R.drawable.sport_white,
            R.drawable.work_white,
            R.drawable.nutrition_white,
            R.drawable.family_white_frame,
            R.drawable.chores_white,
            R.drawable.relax_white,
            R.drawable.friends_white, 0};
    Integer[] catBackgroundFull = //IMPORTANT: DONT CHANGE THE ORDER HERE!!!!
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke,
                    R.drawable.rounded_rec_other_nostroke};
    StringBuilder dateString = new StringBuilder();
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView monthCalendar;
    private RelativeLayout relativeLayout;
   // private TextView eventsName;
    private TextView noItemsText;
    private RecyclerView mRecycler;
    private DatabaseReference schRef;
    private DatabaseReference anchRef;
    private ProgressBar spinner;
    private FirebaseRecyclerOptions<MainModelMonth> options;
    private FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder> fbAdapter;
    private FloatingActionButton addTasks;
    final String[] dateStringSentToAddAnchor = new String[1];
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String UID;

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        addTasks=view.findViewById(R.id.addTasksFloater);
        addTasks.setOnClickListener(this);
        noItemsText=view.findViewById(R.id.noItemsText);
        noItemsText.setText("No schedule/anchors to show");
        noItemsText.setVisibility(View.VISIBLE);
        monthCalendar=view.findViewById(R.id.monthCalendar);
        final HashSet<String> datesWithTasks = new HashSet<>();
        final HashSet<String> datesWithAnchors = new HashSet<>();
        schRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules");
        anchRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Anchors");
        //child of pending tasks that they are tasks
        Query query_sch = schRef;
        Query query_anchors = anchRef;
        query_anchors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    datesWithAnchors.add((String) ds.child("date").getValue());
                }
                /* decorator start */
                monthCalendar.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        StringBuilder dateSB = getSBfromCalendarDay(day);
                        String date = dateSB.toString();
                        if (datesWithAnchors.contains(date))
                            return true;
                        else
                            return false;
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new DotSpan(5, Color.RED));
                    }

                });
                /* decorator end */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        query_sch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    datesWithTasks.add(ds.getKey());
                }
                /* decorator start */
                monthCalendar.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        StringBuilder dateSB = getSBfromCalendarDay(day);
                        String date = dateSB.toString();
                        if (datesWithTasks.contains(date))
                            return true;
                        else
                            return false;
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new DotSpan(5,ContextCompat.getColor(getContext(), R.color.arrangeMeMain)));
                    }

                });
                /* decorator end */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //spinner = view.findViewById(R.id.progressBar55);
        //spinner.setVisibility(View.GONE);
        //relativeLayout = view.findViewById(R.id.relativeLayout);
        //relativeLayout.setVisibility(View.VISIBLE);
        //eventsName = view.findViewById(R.id.eventsName);
        mRecycler = view.findViewById(R.id.eventsRecyclerView);
        mRecycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        /* Calendar stuff */
        monthCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            /**
             * @param widget
             * @param date
             * @param selected
             * function that controls what happens each time we pick a date in month calendar
             */
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //spinner.setVisibility(View.VISIBLE);
                addTasks.setEnabled(true);
                monthCalendar.setSelectionColor(ContextCompat.getColor(getContext(), R.color.arrangeMeMain));
                noItemsText.setVisibility(View.VISIBLE);
                dateString.setLength(0);
                int dayOfMonth = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();
                if(dayOfMonth<10) {
                    dateString.append(0);
                }
                dateString.append(dayOfMonth);
                dateString.append("-");
                if(month+1<10) {
                    dateString.append(0);
                }
                dateString.append(month+1);
                dateString.append("-");
                dateString.append(year);
                dateStringSentToAddAnchor[0] =dateString.toString();
                /* Recycler Stuff */
                schRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules");

                schRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(String.valueOf(dateString))){
                            showOnlyAnchors();
                        }
                        else {
                            addTasks.setEnabled(false); // if schedule exists this date, dont add anchors
                            showSchedule();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /* Recycler Stuff End*/



            };
        });
        /* Calendar stuff End*/

    }

    /**
     * Show schedule for specified day in recycler view
     */
    private void showSchedule() {
        schRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(String.valueOf(dateString)).child("schedule");
        options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(schRef, MainModelMonth.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
                try {
                    holder.timeText.setText(model.getStartTime() + "-" + model.getEndTime());
                    //holder.button.setText("\t"+model.getDescription()+" \n\n\t"+"Category: " + model.getCategory().toLowerCase());
                    String cat = model.getCategory();
                    SpannableStringBuilder str;
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
                    holder.button.setLayoutParams(new LinearLayout.LayoutParams(680, ViewGroup.LayoutParams.MATCH_PARENT));
                    holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
                    holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
                    if (model.getType().equals("anchor")) {
                        holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                        holder.button.setBackgroundResource
                                (R.drawable.rounded_temp_grey_anchor);
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {   //OPEN ANCHOR POPUP
                                String id = model.getAnchorID();
                                Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("AnchorKeyFromWeek", id);
                                b.putString("date", String.valueOf(dateString));
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
                    } else if (model.getType().equals("task")) {
                        holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                        holder.button.setBackgroundResource
                                (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds
                                (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())],
                                        0);
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = model.getActiveKey();
                                Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("TaskKeyFromWeek", id);
                                b.putString("date", String.valueOf(dateString));
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });

                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("TAG", "onCreateViewHolder: " + viewType);
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events_month, parent, false);
                return new MyViewHolder(v);
            }
        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

    }

    /**
     * If there isnt schedule, show only anchors
     */
    private void showOnlyAnchors() {
        Query q = anchRef.orderByChild("date").equalTo(String.valueOf(dateString));
        options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(q, MainModelMonth.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
                //  spinner.setVisibility(View.VISIBLE);
                noItemsText.setVisibility(View.GONE);
                holder.timeText.setText(model.getStartTime() + " - " + model.getEndTime());
                //holder.button.setText("\t"+model.getDescription()+" \n\n\t"+"Category: " + model.getCategory().toLowerCase());
                SpannableStringBuilder str = new SpannableStringBuilder
                        (model.getDescription() + "\n\nCategory : " + model.getCategory());
                str.setSpan(new RelativeSizeSpan(1.3f), 0, model.getDescription().length() + 1, 0);
                str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.button.setText(str);
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(680, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
                //if (model.getType().equals("ANCHOR")) {
                //    holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                // } else if (model.getType().equals("TASK")) {
                //    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                //}
                holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time); //for now it only show anchors
                holder.button.setBackgroundResource
                        (R.drawable.rounded_temp_grey_anchor);
                // spinner.setVisibility(View.GONE);
                holder.button.setOnClickListener(v -> { //goto anchor/task popup
                    schRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String key = fbAdapter.getRef(position).getKey();
                            Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("AnchorKey", key);
                            intent.putExtras(b);
                            startActivity(intent);
                            //for now, only anchors
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                });
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("TAG", "onCreateViewHolder: " + viewType);
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events_month, parent, false);
                return new MyViewHolder(v);
            }
        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

    }

    /**
     * @param v
     * onclick listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.addTasksFloater):

                Intent intent = new Intent(getActivity(), AddAnchor.class);
                intent.putExtra("date", dateStringSentToAddAnchor[0]);
                startActivity(intent);
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    /**
     * @param date
     * @return StringBuilder
     * this function gets CalendarDay and returns StringBuilder of this date
     */
    public StringBuilder getSBfromCalendarDay (CalendarDay date) {
        StringBuilder dateString = new StringBuilder();
        dateString.setLength(0);
        int dayOfMonth = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();
        if (dayOfMonth < 10) {
            dateString.append(0);
        }
        dateString.append(dayOfMonth);
        dateString.append("-");
        if (month + 1 < 10) {
            dateString.append(0);
        }
        dateString.append(month + 1);
        dateString.append("-");
        dateString.append(year);
        return dateString;
    }
}