package com.example.arrangeme.ui.calendar.month;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.schedule.MainModelSchedule;
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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

import cn.pedant.SweetAlert.SweetAlertDialog;


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
        final long[] numOfItems = new long[1];
        addTasks=view.findViewById(R.id.addTasksFloater);
        addTasks.setOnClickListener(this);
        noItemsText=view.findViewById(R.id.noItemsText);
        noItemsText.setText("No tasks/anchors to show");
        noItemsText.setVisibility(View.VISIBLE);
        monthCalendar=view.findViewById(R.id.monthCalendar);
        spinner = view.findViewById(R.id.progressBar55);
        spinner.setVisibility(View.GONE);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.VISIBLE);
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

        /* Calendar stuff */
        monthCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //spinner.setVisibility(View.VISIBLE);
                noItemsText.setVisibility(View.VISIBLE);
                String dateString=date.toString();
                
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
                        holder.button.setLayoutParams(new LinearLayout.LayoutParams(820, ViewGroup.LayoutParams.MATCH_PARENT));
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

                spinner.setVisibility(View.GONE);


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
            ad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    Toast.makeText(getContext(),"Achor pressed", Toast.LENGTH_LONG).show();
                }

            });
            ad.show();
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    //TODO: change the days to english
    //todo: add task/anchor when pressing the +
    //todo: spinner?!?!?12/#?!@$?#@!QR$?@#W
    //todo: background to the focused day if there is task/anchor/none/both
}