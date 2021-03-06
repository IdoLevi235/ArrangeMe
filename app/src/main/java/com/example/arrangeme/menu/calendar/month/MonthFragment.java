package com.example.arrangeme.menu.calendar.month;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arrangeme.Anchors.AddAnchor;
import com.example.arrangeme.Anchors.AnchorPagePopup;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.calendar.FilterFragment;
import com.example.arrangeme.menu.tasks.TaskPagePopup;
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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;


/**
 * @param <RecyclerAdapter>
 * This class controls the month view calendar
 */
public class MonthFragment<RecyclerAdapter> extends Fragment implements  View.OnClickListener {
    Integer[] catIconWhite = {R.drawable.study_white,
            R.drawable.sport_white,
            R.drawable.work_white,
            R.drawable.nutrition_white,
            R.drawable.family_white,
            R.drawable.chores_white,
            R.drawable.relax_white,
            R.drawable.friends_white, 0};
    Integer[] catIcon = {R.drawable.study, R.drawable.sport,
            R.drawable.work, R.drawable.nutrition,
            R.drawable.familycat, R.drawable.chores,
            R.drawable.relax, R.drawable.friends_cat, 0};
    Integer[] catBackgroundFull = //IMPORTANT: DONT CHANGE THE ORDER HERE!!!!
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke,
                    R.drawable.rounded_rec_other_nostroke};
    StringBuilder dateString = new StringBuilder();
    Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
            R.color.family, R.color.chores, R.color.relax,R.color.friends, R.color.other};
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
        monthCalendar=view.findViewById(R.id.monthCalendar);
        noItemsText=view.findViewById(R.id.textView8);
        final HashSet<String> datesWithTasks = new HashSet<>();
        final HashSet<String> datesWithAnchors = new HashSet<>();
        schRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules");
        anchRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Anchors");
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

        Query query_sch = schRef;
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);

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

        setToday();

    }

    private void setToday() {
        monthCalendar.setSelectedDate(CalendarDay.today());
        monthCalendar.setSelectionColor(ContextCompat.getColor(getContext(), R.color.arrangeMeMain));
        dateString = getSBfromCalendarDay(CalendarDay.today());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    /**
     * Show schedule for specified day in recycler view
     */
    protected void showSchedule() {
        noItemsText.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        schRef = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules").child(String.valueOf(dateString)).child("schedule");
        //check
        options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(schRef, MainModelMonth.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
                try {

                    if (!FilterFragment.Category_Set.isEmpty() &&
                            !FilterFragment.Category_Set.contains(model.getCategory()))
                    { // categories filter
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }

                    if ((model.getType().equals("anchor") && FilterFragment.typeFilter==1) ||
                            ((model.getType().equals("task") && FilterFragment.typeFilter==2)))
                    {
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                    holder.timeText.setText(model.getStartTime() + "-" + model.getEndTime());
                    //holder.button.setText("\t"+model.getDescription()+" \n\n\t"+"Category: " + model.getCategory().toLowerCase());
                    String cat = model.getCategory();
                    SpannableStringBuilder str;
                    if(cat!=null) {
                        str = new SpannableStringBuilder
                                (model.getDescription() + "\nCategory : " + cat);
                    }
                    else {
                        str = new SpannableStringBuilder(model.getDescription());
                    }
                    str.setSpan(new RelativeSizeSpan(1.3f), 0, model.getDescription().length(), 0);
                    str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.button.setText(str);
                   // holder.button.setLayoutParams(new LinearLayout.LayoutParams(680, ViewGroup.LayoutParams.MATCH_PARENT));
                   // holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
                   holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
                    if (model.getType().equals("anchor")) {
                        holder.button.setTextColor(ContextCompat.getColor(getContext(), R.color.anchor));
                        holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                        holder.button.setBackgroundResource
                                (R.drawable.category_btn_schedule);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds
                                (0, 0, R.drawable.anchor,
                                        0);

                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {   //OPEN ANCHOR POPUP
                                String id = model.getAnchorID();
                                Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("AnchorKeyFromWeek", id);
                                b.putString("date", String.valueOf(dateString));
                                if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                    b.putString("photo", "yes");
                                }
                                else {
                                    b.putString("photo", "no");
                                }
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
                    } else if (model.getType().equals("task")) {
                        holder.button.setTextColor(ContextCompat.getColor(getContext(), catColor[TaskCategory.fromStringToInt(model.getCategory())]));
                        holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                        holder.button.setBackgroundResource
                                (R.drawable.category_btn_schedule);
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
                                if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                    b.putString("photo", "yes");
                                }
                                else {
                                    b.putString("photo", "no");
                                }
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

        //check if there are anchors, if not show msg
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0) {// no anchors (and no schedule)
                    noItemsText.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.GONE);
                }
                else showAnchors(q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showAnchors(Query q) {
        noItemsText.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(q, MainModelMonth.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
                //  spinner.setVisibility(View.VISIBLE);
                holder.timeText.setText(model.getStartTime() + " - " + model.getEndTime());
                //holder.button.setText("\t"+model.getDescription()+" \n\n\t"+"Category: " + model.getCategory().toLowerCase());
                SpannableStringBuilder str = new SpannableStringBuilder
                        (model.getDescription() + "\nAnchors " + model.getCategory());
                str.setSpan(new RelativeSizeSpan(1.3f), 0, model.getDescription().length() + 1, 0);
                str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.button.setText(str);
                holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
                holder.button.setTextColor(ContextCompat.getColor(getContext(), R.color.anchor));
                holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                holder.button.setBackgroundResource(R.drawable.category_btn_schedule);
                holder.button.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, R.drawable.anchor,
                                0);
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
                            if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                b.putString("photo", "yes");
                            }
                            else {
                                b.putString("photo", "no");
                            }
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