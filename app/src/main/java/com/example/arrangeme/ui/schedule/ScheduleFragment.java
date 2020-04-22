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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.AddTasks.MainAdapter;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.calendar.DayFragment;
import com.example.arrangeme.ui.calendar.MonthFragment;
import com.example.arrangeme.ui.calendar.WeekFragment;
import com.example.arrangeme.ui.myprofile.MyProfileViewModel;

import java.util.List;

public class ScheduleFragment<RecyclerAdapter> extends Fragment implements View.OnClickListener {
    private ScheduleViewModel scheduleViewModel;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView chooseMessage;
    private Button chooseTaskBtn;
    private Button questionnaireBtn;
    private RecyclerView recyclerSchedule;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    List <String> tasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        tasks.add("First check");
        tasks.add("Second check");
        tasks.add("third check");
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

        mAdapter = new AdapterScheduleTab(tasks);
        recyclerSchedule.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration( getActivity(),DividerItemDecoration.VERTICAL);
        recyclerSchedule.addItemDecoration(dividerItemDecoration);



        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());


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
