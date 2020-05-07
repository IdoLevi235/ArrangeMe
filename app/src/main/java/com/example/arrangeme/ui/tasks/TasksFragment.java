
package com.example.arrangeme.ui.tasks;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.AddTasks.MainAdapter;
import com.example.arrangeme.AddTasks.MainModel;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.MainActivity;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.Signup;
import com.example.arrangeme.ui.tasks.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class TasksFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecycler;
    private ArrayList<MainModelTasks> mainModels;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelTasks> options;
    private FirebaseRecyclerAdapter<MainModelTasks, MyViewHolder> fbAdapter;
    private TasksViewModel tasksViewModel;
    private String deletedCategory;
    private String deletedDescription;
    private String deletedLocation;
    private String deletedKey;
    private FloatingActionButton addTasks;
    private ProgressBar spinner;
    private TextView tv;
    private LinearLayoutManager layoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        return root;
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (ProgressBar)view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        addTasks = (FloatingActionButton)view.findViewById(R.id.add);
        addTasks.setOnClickListener(this);
        tv = view.findViewById(R.id.textView7);
        tv.setVisibility(View.GONE);

        /* Recycler view stuff */
        mRecycler= view.findViewById(R.id.recyclerTasks);
        setRecycler(mRecycler);
        /* Recycler view stuff End*/

        /* swipe stuff */
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
                        deletedKey = fbAdapter.getRef(position).getKey();
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                deletedCategory = (String) dataSnapshot.child(deletedKey).child("category").getValue();
                                deletedDescription = (String) dataSnapshot.child(deletedKey).child("description").getValue();
                                deletedLocation = (String) dataSnapshot.child(deletedKey).child("location").getValue();

                                Snackbar.make(mRecycler,"You deleted a " + deletedCategory
                                        +" task: " + deletedDescription ,Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDatabase.child(deletedKey).child("category").setValue(deletedCategory);
                                                mDatabase.child(deletedKey).child("description").setValue(deletedDescription);
                                                mDatabase.child(deletedKey).child("location").setValue(deletedLocation);
                                            }
                                        }).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        fbAdapter.getRef(position).setValue(null);


                        break;
                    default:
                        break;
                }

            }

            @SuppressLint("ResourceType")
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                       // .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.redtrash))
                       // .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                       // .create()
                       // .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            }
        });
        helper.attachToRecyclerView(mRecycler);
        /* swipe stuff end */
    }

    private void setRecycler(RecyclerView mRecycler) {
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,  R.drawable.work_white, R.drawable.nutrition_white, R.drawable.family_white,R.drawable.chores_white, R.drawable.relax_white,R.drawable.friends_white,  0};
        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport, R.drawable.category_btn_work, R.drawable.category_btn_nutrition, R.drawable.category_btn_family, R.drawable.category_btn_chores, R.drawable.category_btn_relax,R.drawable.category_btn_friends, R.drawable.category_btn_other};
        Integer[] catBackgroundFull =
                {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke, R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke, R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke, R.drawable.rounded_rec_relax_nostroke,R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_other_nostroke};
        Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family,R.color.chores, R.color.relax,R.color.friends,  R.color.other};
        String[] catName = {"Study", "Sport", "Work","Nutrition", "Family","Chores", "Relax","Friends" , "Other"};
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        options = new FirebaseRecyclerOptions.Builder<MainModelTasks>().setQuery(mDatabase,MainModelTasks.class).build();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0) {
                    tv.setText("You have no pending tasks");
                    tv.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fbAdapter=new FirebaseRecyclerAdapter<MainModelTasks, MyViewHolder>(options) {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelTasks model) {
                holder.button.setText("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
                holder.button.setLayoutParams (new LinearLayout.LayoutParams(850, ViewGroup.LayoutParams.MATCH_PARENT));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                holder.button.setBackgroundResource(catBackgroundFull[x]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds (0,0,catIcon[x],0);
                holder.button.setOnClickListener(v -> {
                    //todo:task page with edit/view
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        //This method gets the task key from the database and pass it to the next activity with a bundle
                        @Override

                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //TODO: receive the id of the
                            //String taskKey = dataSnapshot.getKey();
                            String taskKey = fbAdapter.getRef(position).getKey();
                            Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("TaskKey", taskKey);
                            intent.putExtras(b);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                });
                spinner.setVisibility(View.GONE);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
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
                //Button btn1 = (Button) ad.findViewById(R.id.confirm_button);
               // btn1.setBackgroundResource(R.drawable.rounded_rec);




        }
    }
}