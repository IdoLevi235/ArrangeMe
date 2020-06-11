
package com.example.arrangeme.menu.tasks;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.calendar.FilterFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TasksFragment extends Fragment implements View.OnClickListener {
    ArrayList<String> keys;
    private FrameLayout containerFilter;
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
    private RelativeLayout rlNotasks;
    private TextView noTask;
    private int flag = 0;
    private LinearLayoutManager layoutManager;
    Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,
            R.drawable.work_white, R.drawable.nutrition_white,
            R.drawable.family_white, R.drawable.chores_white,
            R.drawable.relax_white, R.drawable.friends_white, 0};
    Integer[] catBackgroundFull =
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke,
                    R.drawable.rounded_rec_other_nostroke};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        containerFilter = root.findViewById(R.id.filter_container);
        setHasOptionsMenu(true);
        return root;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG8", "onViewCreated: HELLO");
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        addTasks = (FloatingActionButton) view.findViewById(R.id.add);
        addTasks.setOnClickListener(this);
        tv = view.findViewById(R.id.textView7);
        tv.setVisibility(View.GONE);
        rlNotasks = view.findViewById(R.id.noTasksLayout);
        rlNotasks.setVisibility(View.GONE);
        noTask = view.findViewById(R.id.noTasks);
        noTask.setVisibility(View.GONE);


        /* Recycler view stuff */
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler = view.findViewById(R.id.recyclerTasks);
        setRecycler(mRecycler);
        /* Recycler view stuff End*/

        /* swipe stuff */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        deletedKey = fbAdapter.getRef(position).getKey();
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                deletedCategory = (String) dataSnapshot.child(deletedKey).child("category").getValue();
                                deletedDescription = (String) dataSnapshot.child(deletedKey).child("description").getValue();
                                deletedLocation = (String) dataSnapshot.child(deletedKey).child("location").getValue();

                                Snackbar.make(mRecycler, "You deleted a " + deletedCategory
                                        + " task: " + deletedDescription, Snackbar.LENGTH_LONG)
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

    public void setRecycler(RecyclerView mRecycler) {
        keys = new ArrayList<>();
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
        options = new FirebaseRecyclerOptions.Builder<MainModelTasks>().setQuery(mDatabase, MainModelTasks.class).build();
        checkIfThereArePendingTasks(mDatabase);
        /* Fire base UI stuff */
        fbAdapter = new FirebaseRecyclerAdapter<MainModelTasks, MyViewHolder>(options) {
            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelTasks model) {
//                if (FilterFragment.Category_Set.contains(model.getCategory().toUpperCase())){ // filter hide
//                    holder.itemView.setVisibility(View.INVISIBLE);
//                }
                holder.button.setText("\t" + model.getCategory() + " \n\n\t" + model.getDescription());
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(850, ViewGroup.LayoutParams.MATCH_PARENT));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                holder.button.setBackgroundResource(catBackgroundFull[x]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
                holder.button.setOnClickListener(v -> {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        //This method gets the task key from the database and pass it to the next activity with a bundle
                        @Override

                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //String taskKey = dataSnapshot.getKey();
                            String taskKey = fbAdapter.getRef(position).getKey();
                            Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_tab, parent, false);
                return new MyViewHolder(v);
            }
        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);
        /* Fire base UI stuff End */

    }

    private void checkIfThereArePendingTasks(DatabaseReference mDatabase) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    spinner.setVisibility(View.GONE);
                    rlNotasks.setVisibility(View.VISIBLE);
                    noTask.setVisibility(View.VISIBLE);
                    noTask.setText("You have currently no tasks. \n\n Press the Plus button below to add some new tasks.");
                    addTasks.setElevation(99);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                SweetAlertDialog ad;
                startActivity(new Intent(getActivity(), AddTasks.class));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.filterIcon) {
            if (flag == 0)
                openFilterFragment();
            else if (flag == 1) {
                closeFilterFragment();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void closeFilterFragment() {
        flag = 0;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void openFilterFragment() {
        flag = 1;
        FilterFragmentTask filterFragmentTask = new FilterFragmentTask();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.filter_container, filterFragmentTask, "Blank").commit();
    }
}