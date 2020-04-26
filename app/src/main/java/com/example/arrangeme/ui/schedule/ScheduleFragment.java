package com.example.arrangeme.ui.schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerSchedule.setLayoutManager(layoutManager);
        recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        //RecyclerView.ItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        //recyclerSchedule.addItemDecoration(divider);

        Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,  R.drawable.work_white,R.drawable.friends_white, R.drawable.nutrition_white, R.drawable.family_white_frame, R.drawable.chores_white, R.drawable.relax_white, 0};

        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport,
                R.drawable.category_btn_work,R.drawable.category_btn_friends, R.drawable.category_btn_nutrition,
                R.drawable.category_btn_family, R.drawable.category_btn_chores,
                R.drawable.category_btn_relax, R.drawable.category_btn_other};
        Integer[] catBackgroundFull =
                {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                        R.drawable.rounded_rec_work_nostroke,R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                        R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                        R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_other_nostroke};
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
                holder.button.setLayoutParams (new LinearLayout.LayoutParams(720, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));

                if(model.getType().equals("ANCHOR"))
                    holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                else if (model.getType().equals("TASK"))
                    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);

                switch (model.getCategory()){
                    case "STUDY":
                        holder.button.setBackgroundResource(catBackgroundFull[0]);
                        //holder.button.setBackgroundResource(catBackground[0]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[0],0);
                        break;
                    case "SPORT":
                        holder.button.setBackgroundResource(catBackgroundFull[1]);
                        //holder.button.setBackgroundResource(catBackground[1]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[1],0);
                        break;
                    case "WORK":
                        holder.button.setBackgroundResource(catBackgroundFull[2]);
                        //holder.button.setBackgroundResource(catBackground[2]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[2],0);
                        break;
                    case "FRIENDS":
                        holder.button.setBackgroundResource(catBackgroundFull[3]);
                        //holder.button.setBackgroundResource(catBackground[3]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[3],0);
                        break;
                    case "NUTRITION":

                        holder.button.setBackgroundResource(catBackgroundFull[4]);
                       //holder.button.setBackgroundResource(catBackground[4]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[4],0);
                        break;
                    case "FAMILY":
                        //holder.button.setBackgroundResource(catBackground[5]);
                        holder.button.setBackgroundResource(catBackgroundFull[5]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[5],0);
                        break;
                    case "CHORES":
                        holder.button.setBackgroundResource(catBackgroundFull[6]);
                        //holder.button.setBackgroundResource(catBackground[6]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[6],0);
                        break;
                    case "RELAX":
                        holder.button.setBackgroundResource(catBackgroundFull[7]);
                        //holder.button.setBackgroundResource(catBackground[7]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[7],0);
                        break;
                    case "OTHER":
                        holder.button.setBackgroundResource(catBackgroundFull[8]);
                        //holder.button.setBackgroundResource(catBackground[8]);
                        holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[8],0);

                        break;
                }
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
                DatabaseReference firstItemRef = fbAdapter.getRef(dragged.getAdapterPosition());
                DatabaseReference secondItemRef = fbAdapter.getRef(target.getAdapterPosition());
                DatabaseReference tempRef;
                fbAdapter.notifyItemMoved(position_dragged, position_target);
               final String firstKey = firstItemRef.getKey();
               final String secondKey = secondItemRef.getKey();

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                        // IMPORTANT: when we edit this to fir schedule, we must not "switch" between
                        // startTime + endTime of this task!!!!!
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
                //Collections.swap(mainModels,position_dragged,position_target);
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
