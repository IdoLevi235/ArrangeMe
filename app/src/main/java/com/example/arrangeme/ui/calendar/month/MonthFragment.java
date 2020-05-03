package com.example.arrangeme.ui.calendar.month;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.schedule.MainModelSchedule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;


public class MonthFragment<RecyclerAdapter> extends Fragment implements  View.OnClickListener {
    private CalendarView monthCalendar;
    private Switch switchCat;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelMonth> options;
    private FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder> fbAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        monthCalendar = view.findViewById(R.id.monthCalendar);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchCat = view.findViewById(R.id.switchCat);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.GONE);
        eventsName = view.findViewById(R.id.eventsName);
        switchCat.setOnClickListener(this);
        monthCalendar.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
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


        options = new FirebaseRecyclerOptions.Builder<MainModelMonth>().setQuery(mDatabase, MainModelMonth.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelMonth, MyViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelMonth model) {
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
                else if (model.getType().equals("TASK")) {
                    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                }
                holder.button.setBackgroundResource
                        (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds
                        (0,0,catIcon[TaskCategory.fromStringToInt(model.getCategory())],0);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events_month,parent,false);
                return new MyViewHolder(v);
            }

        };

        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.switchCat): {
                if (switchCat.isChecked()) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    //TODO: change the days to english
    //todo: add task/anchor when pressing the +
}