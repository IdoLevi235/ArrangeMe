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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ScheduleFragment<RecyclerAdapter> extends Fragment implements View.OnClickListener {
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        questionnaireBtn =  view.findViewById(R.id.questionnaireBtn);
        chooseTaskBtn = view.findViewById(R.id.chooseTaskBtn);
        chooseMessage = view.findViewById(R.id.chooseMessage);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet= view.findViewById(R.id.quesMessage);
        recyclerSchedule= view.findViewById(R.id.recyclerSchedule);
        recyclerSchedule.setHasFixedSize(true);
        //TODO: function that checks if there is a schedule, it means if the user chose tasks for today & fill the questionnaire(personality vector is ful), if not, visible the texts that I did.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerSchedule.addItemDecoration(divider);
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase,MainModelSchedule.class).build();
        fbAdapter=new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                holder.button.setText(model.getCategory());
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

        // Drag and drop stuff //
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                Log.d("TAG", "onMove: inside onMove");
                Collections.swap(mainModels,position_dragged,position_target);
                fbAdapter.notifyItemMoved(position_dragged,position_target);
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


//TODO: while schedule is loading put something that spins with "loading schedule...."
