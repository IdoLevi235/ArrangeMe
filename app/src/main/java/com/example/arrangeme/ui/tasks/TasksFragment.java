
package com.example.arrangeme.ui.tasks;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.MainAdapter;
import com.example.arrangeme.AddTasks.MainModel;
import com.example.arrangeme.Globals;
import com.example.arrangeme.MainActivity;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class TasksFragment extends Fragment {
    private RecyclerView mRecycler;
    private ArrayList<MainModelTasks> mainModels;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelTasks> options;
    private FirebaseRecyclerAdapter<MainModelTasks, MyViewHolder> fbAdapter;
    private TasksViewModel tasksViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        return root;
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler= view.findViewById(R.id.recyclerTasks);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
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
        String[] catName = {"Study", "Sport", "Work", "Friends" ,"Nutrition", "Family", "Chores", "Relax", "Other"};
        options = new FirebaseRecyclerOptions.Builder<MainModelTasks>().setQuery(mDatabase,MainModelTasks.class).build();
        fbAdapter=new FirebaseRecyclerAdapter<MainModelTasks, MyViewHolder>(options) {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelTasks model) {
                holder.button.setText("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
                holder.button.setLayoutParams (new LinearLayout.LayoutParams(750, ViewGroup.LayoutParams.MATCH_PARENT));

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
                    default:
                        break;
                }
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_tab,parent,false);
                return new MyViewHolder(v);
            }
        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                switch(direction){
                    case ItemTouchHelper.LEFT:

                        //fbAdapter.notifyItemRemoved(position);
                        break;
                    default:
                        break;
                }

            }

            @SuppressLint("ResourceType")
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        helper.attachToRecyclerView(mRecycler);

    }
}