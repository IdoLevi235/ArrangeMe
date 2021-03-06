
package com.example.arrangeme.menu.tasks;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;


/**
 * Class that controls  tasks fragment
 */
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
    private View view4;
    private RelativeLayout rlNotasks;
    private TextView noTask;
    private TextView noTask2;
    private int flag = 0;
    private LinearLayoutManager layoutManager;
//    Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,
//            R.drawable.work_white, R.drawable.nutrition_white,
//            R.drawable.family_white, R.drawable.chores_white,
//            R.drawable.relax_white, R.drawable.friends_white, 0};
    Integer[] catIcon = {R.drawable.study, R.drawable.sport,
            R.drawable.work, R.drawable.nutrition,
            R.drawable.familycat, R.drawable.chores,
            R.drawable.relax, R.drawable.friends_cat, 0};
    Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
            R.color.family, R.color.chores, R.color.relax,R.color.friends, R.color.other};
    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        containerFilter = root.findViewById(R.id.filter_container);
        setHasOptionsMenu(true);
        return root;
    }


    /**
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view4 = view.findViewById(R.id.view4);
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        addTasks = (FloatingActionButton) view.findViewById(R.id.add);
        addTasks.setOnClickListener(this);
        rlNotasks = view.findViewById(R.id.noTasksLayout);
        rlNotasks.setVisibility(View.GONE);
        noTask = view.findViewById(R.id.noTasks);
        noTask2= view.findViewById(R.id.noTasks2);
        noTask.setVisibility(View.GONE);
        tv = view.findViewById(R.id.textView6);


        /* Recycler view stuff */
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler = view.findViewById(R.id.recyclerTasks);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
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
                        checkIfThereArePendingTasks(mDatabase);

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

        tutorial();
    }

    /**
     * defining the tutorial in the start of the app
     */
    private void tutorial() {

        //this function is the help system
        if (Globals.isNewUser == true) {
            if (Globals.tutorial == 1) {
                Globals.tutorial++;
                new MaterialTapTargetPrompt.Builder(getActivity()).setTarget(R.id.add).setPrimaryText("Click to add a new task").setTextGravity(Gravity.CENTER).setSecondaryText("In order to build a schedule you need to add new tasks.").setBackgroundColour(Color.parseColor("#20666E")).show();
            }
            if (Globals.tutorial == 5) {
                Globals.tutorial++;
                new MaterialTapTargetPrompt.Builder(this).setTextGravity(Gravity.CENTER).setTarget(R.id.navigation_calendar).setPrimaryText("Here you can add anchors to the calendar").setSecondaryText("After assigning tasks, you can assign anchors. Anchors are fixed parts in the day like important meetings or weddings").setBackgroundColour(Color.parseColor("#20666E")).show();
            }
        }
    }

    /**
     * setting recycler view
     * @param mRecycler
     */
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


                holder.button.setText("\t  " + model.getCategory() + " \n\n\t  " + model.getDescription());
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(850, ViewGroup.LayoutParams.MATCH_PARENT));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                holder.button.setBackgroundResource(R.drawable.category_btn_schedule);
                String cat = model.getCategory();
                SpannableStringBuilder str = null;
                if(cat!=null) {
                    str = new SpannableStringBuilder
                            (model.getDescription() + "\n\nCategory : " + cat);
                }
                else {
                    str = new SpannableStringBuilder(model.getDescription());
                }
                str.setSpan(new RelativeSizeSpan(1.5f), 0, model.getDescription().length(), 0);
                str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, model.getDescription().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.button.setText(str);
                holder.button.setTextColor(ContextCompat.getColor(getContext(), catColor[TaskCategory.fromStringToInt(model.getCategory())]));
                holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
                holder.button.setPadding(15,10,15,0);
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
                            if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                b.putString("photo", "yes");
                            }
                            else {
                                b.putString("photo", "no");
                            }

                            intent.putExtras(b);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                });
                spinner.setVisibility(View.GONE);
                if (Globals.isNewUser == true) {
                    if(Globals.tutorial==3){
                    Globals.tutorial++;
                    new MaterialTapTargetPrompt.Builder(getActivity()).setPromptFocal(new RectanglePromptFocal()).setTarget(holder.button).setTextGravity(Gravity.CENTER).setClipToView(mRecycler.getChildAt(0)).setPrimaryText("Click to watch and edit task details").setSecondaryText("You can delete tasks by swapping left").setBackgroundColour(Color.parseColor("#20666E")).show();
                    }
                }

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

    /**
     * Check if there are any pending task - if not, show message
     * @param mDatabase
     */
    private void checkIfThereArePendingTasks(DatabaseReference mDatabase) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    tv.setVisibility(View.INVISIBLE);
                    view4.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.GONE);
                    rlNotasks.setVisibility(View.VISIBLE);
                    noTask.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.INVISIBLE);
                    noTask.setText("You have currently no tasks");
                    noTask2.setText("Press the Plus button below to add some new tasks");
                    addTasks.setElevation(99);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                SweetAlertDialog ad;
                startActivity(new Intent(getActivity(), AddTasks.class));
        }
    }

    /**
     * Create toolbar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * toolbar click listener
     * @param item
     * @return
     */
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

    /**
     * Close fiilter
     */
    private void closeFilterFragment() {
        flag = 0;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * open filter
     */
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