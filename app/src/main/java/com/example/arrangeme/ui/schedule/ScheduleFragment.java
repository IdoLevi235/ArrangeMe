package com.example.arrangeme.ui.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.AddTasks.MainAdapter;
import com.example.arrangeme.AddTasks.MainModel;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.tasks;
import com.example.arrangeme.ui.calendar.DayFragment;
import com.example.arrangeme.ui.calendar.MonthFragment;
import com.example.arrangeme.ui.calendar.WeekFragment;
import com.example.arrangeme.ui.myprofile.MyProfileViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleFragment<RecyclerAdapter> extends Fragment implements View.OnClickListener {
    private ScheduleViewModel scheduleViewModel;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView chooseMessage;
    private Button chooseTaskBtn;
    private Button questionnaireBtn;
    private RecyclerView recyclerSchedule;
    private AdapterScheduleTab mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private ArrayList<MainModelSchedule> mainModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
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
        questionnaireBtn =  view.findViewById(R.id.questionnaireBtn);
        chooseTaskBtn = view.findViewById(R.id.chooseTaskBtn);
        chooseMessage = view.findViewById(R.id.chooseMessage);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet= view.findViewById(R.id.quesMessage);
        recyclerSchedule= view.findViewById(R.id.recyclerSchedule);
        //TODO: function that checks if there is a schedule, it means if the user chose tasks for today & fill the questionnaire(personality vector is ful), if not, visible the texts that I did.
        //checkIfScheduleExists();
        String[] checks = {"1", "2", "3", "4", "5", "6", "7", "8"};
        Integer[] icons ={R.drawable.alone, R.drawable.basket, R.drawable.anchor, R.drawable.note,
                R.drawable.pill, R.drawable.pizza, R.drawable.star,R.drawable.relax};
        mainModels = new ArrayList<>();
        for (int i = 0; i < checks.length; i++) {
            MainModelSchedule model = new MainModelSchedule(checks[i], icons[i]);
            mainModels.add(model);
        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterScheduleTab(getContext(),mainModels);
        recyclerSchedule.setAdapter(mAdapter);
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerSchedule.addItemDecoration(divider);
        // Drag and drop stuff //
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                Log.d("TAG", "onMove: inside onMove");
                Collections.swap(mainModels,position_dragged,position_target);
                mAdapter.notifyItemMoved(position_dragged,position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        // Drag and drop stuff end//
        helper.attachToRecyclerView(recyclerSchedule);
    }

    @Override
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
