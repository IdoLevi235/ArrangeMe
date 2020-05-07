package com.example.arrangeme.ui.calendar.month;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.AddAnchor;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.schedule.MainModelSchedule;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import org.w3c.dom.Text;

import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Intent.getIntent;


public class MonthFragment<RecyclerAdapter> extends Fragment implements  View.OnClickListener {
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView monthCalendar;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private TextView noItemsText;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private ProgressBar spinner;
    private FirebaseRecyclerOptions<MainModelMonth> options;
    private FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder> fbAdapter;
    private FloatingActionButton addTasks;
    final String[] dateStringSentToAddAnchor = new String[1];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addTasks=view.findViewById(R.id.addTasksFloater);
        addTasks.setOnClickListener(this);
        noItemsText=view.findViewById(R.id.noItemsText);
        noItemsText.setText("No tasks/anchors to show");
        noItemsText.setVisibility(View.VISIBLE);
        monthCalendar=view.findViewById(R.id.monthCalendar);
        final HashSet<String> datesWithTasks = new HashSet<>();
        final HashSet<String> datesWithAnchors = new HashSet<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        //child of pending tasks that they are tasks
        Query query_tasks = mDatabase.orderByChild("type").equalTo("TASK");
        Query query_anchors = mDatabase.orderByChild("type").equalTo("ANCHOR");
        query_anchors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    datesWithAnchors.add((String) ds.child("createDate").getValue());
                }
                /* decorator start */
                monthCalendar.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        Log.d("TAG", "shouldDecorate: anchors = " + datesWithAnchors);
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


        query_tasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    datesWithTasks.add((String) ds.child("createDate").getValue());
                }
                /* decorator start */
                monthCalendar.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        Log.d("TAG", "shouldDecorate: tasks = " + datesWithTasks);
                        StringBuilder dateSB = getSBfromCalendarDay(day);
                        String date = dateSB.toString();
                        if (datesWithTasks.contains(date))
                            return true;
                        else
                            return false;
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new DotSpan(5, Color.GREEN));
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
        eventsName = view.findViewById(R.id.eventsName);
        mRecycler = view.findViewById(R.id.eventsRecyclerView);
        mRecycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
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

        /* Calendar stuff */
        monthCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //spinner.setVisibility(View.VISIBLE);
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
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
                Query query = mDatabase.orderByChild("createDate").equalTo(dateString.toString());
                options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(query, MainModelMonth.class).build();
                fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
                        //  spinner.setVisibility(View.VISIBLE);
                        noItemsText.setVisibility(View.GONE);
                        holder.timeText.setText(" " + model.getTime());
                        holder.button.setText("\t" + model.getCategory() + " \n\n\t" + model.getDescription());
                        SpannableStringBuilder str = new SpannableStringBuilder
                                ("\t" + model.getCategory() + " \n\n\t" + model.getDescription());
                        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, model.getCategory().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.setSpan(new RelativeSizeSpan(1.05f), 0, model.getCategory().length() + 1, 0);
                        holder.button.setText(str);
                        holder.button.setLayoutParams(new LinearLayout.LayoutParams(680, ViewGroup.LayoutParams.MATCH_PARENT));
                        holder.timeText.setLayoutParams(new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
                        holder.anchorOrTask.setLayoutParams(new LinearLayout.LayoutParams(80, 76));
                        if (model.getType().equals("ANCHOR")) {
                            holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                        } else if (model.getType().equals("TASK")) {
                            holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                        }
                        holder.button.setBackgroundResource
                                (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds
                                (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())], 0);
                        // spinner.setVisibility(View.GONE);

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
                /* Recycler Stuff End*/



            };
        });
        /* Calendar stuff End*/

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.addTasksFloater):
            SweetAlertDialog ad;
            ad =  new SweetAlertDialog( getActivity(), SweetAlertDialog.NORMAL_TYPE)
                    .setContentText(("Do you want to add a task or an anchor?"));
            ad.setConfirmText("Task");
            ad.setCancelText("Anchor");
            ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    startActivity(new Intent(getActivity(), AddTasks.class));
                }
            });
                Intent intent = new Intent(getActivity(), AddAnchor.class);
                ad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    Log.d("TAGAG", "onClick: " + dateStringSentToAddAnchor[0]);
                    intent.putExtra("date", dateStringSentToAddAnchor[0]);
                    startActivity(intent);
           }

            });
            ad.show();
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
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
    //TODO: change the days to english
    //todo: spinner?!?!?12/#?!@$?#@!QR$?@#W
    //todo: background to the focused day if there is task/anchor/none/both
}