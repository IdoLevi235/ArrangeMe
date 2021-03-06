package com.example.arrangeme.menu.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.Anchors.AnchorPagePopup;
import com.example.arrangeme.BuildSchedule.ChooseTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.StartScreens.MainActivity;
import com.example.arrangeme.menu.schedule.ScheduleFeedback;
import com.example.arrangeme.menu.schedule.MainModelSchedule;
import com.example.arrangeme.menu.schedule.MyViewHolder;
import com.example.arrangeme.PersonalityVectorValidate;
import com.example.arrangeme.menu.schedule.ScheduleFragment;
import com.example.arrangeme.menu.tasks.TaskPagePopup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Class that controls the dashboard fragment
 */
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class DashboardFragment extends Fragment implements View.OnClickListener, PersonalityVectorValidate {

    private DashboardViewModel dashboardViewModel;
    private ScheduleFragment sf ;
    private String today;
    private LinearLayoutManager layoutManager;
    private Button chooseTasksBtn;
    private Button questionnaireBtn;
    private ImageView imageView7;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private FirebaseRecyclerOptions<MainModelSchedule> options;
    private FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder> fbAdapter;
    private RelativeLayout noSchRelative;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView welcome;
    private TextView rateMsg;
    private ProgressBar progressBar5;
    private RelativeLayout view11;
    private AppCompatImageView rankit;
    private ProgressBar progressBar6;
    private TextView hello;
    private TextView ScheduleForToday;
    private int count=0;
    Integer[] catIcon = {R.drawable.study, R.drawable.sport,
            R.drawable.work, R.drawable.nutrition,
            R.drawable.familycat, R.drawable.chores,
            R.drawable.relax, R.drawable.friends_cat, 0};
    Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
            R.color.family, R.color.chores, R.color.relax,R.color.friends, R.color.other};
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String UID;
    private boolean flag;
    private int firstPosition;


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);

        return root;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        rateMsg=view.findViewById(R.id.rateMsg);
        hello=view.findViewById(R.id.Hello);
        ScheduleForToday=view.findViewById(R.id.ScheduleForToday);
        welcome=view.findViewById(R.id.welcomText);
        welcome.setText("Welcome " + Globals.currentUsername +"!");
        chooseTasksBtn = view.findViewById(R.id.chooseTasksBtn);
        chooseTasksBtn.setOnClickListener(this);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet = view.findViewById(R.id.noScheduleYet);
        noSchRelative = view.findViewById(R.id.noScheduleLayout);
        noSchRelative.setVisibility(View.GONE);
        questionnaireBtn = view.findViewById(R.id.questionnaireBtn);
        questionnaireBtn.setOnClickListener(this);
        mRecycler=view.findViewById(R.id.recyclerDash);
        mRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(), layoutManager.getOrientation());
        Drawable div = ContextCompat.getDrawable(getContext(),R.drawable.divider);
        dividerItemDecoration.setDrawable(div);
        mRecycler.addItemDecoration(dividerItemDecoration);
        rankit = view.findViewById(R.id.rankit);
        rankit.setOnClickListener(this);
        view11=view.findViewById(R.id.view11);
        view11.setOnClickListener(this);
        progressBar6=view.findViewById(R.id.progressBar6);
        imageView7=view.findViewById(R.id.imageView7);
        imageView7.setImageResource(0);
        progressBar6.setVisibility(View.VISIBLE);
        setGenderPhoto();



        this.checkIfPersonalityVectorFilled();
    }

    /**
     * Setting boy/girl/none photo in the top of the dashboard
     */
    private void setGenderPhoto() {
        DatabaseReference quesRef=  FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("personality_vector").child("1");
        quesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue().toString().equals("1")){
                    imageView7.setImageResource(R.drawable.try2);
                }
                else if (dataSnapshot.getValue().toString().equals("2")) {
                    imageView7.setImageResource(R.drawable.try1);
                }
                else if (dataSnapshot.getValue().toString().equals("3")){
                    imageView7.setImageResource(R.drawable.try3);
                }
                else {
                    imageView7.setImageResource(R.drawable.try1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Showing todays schedule
     */
    private void showTodaySchedule() {
        showScheduleRate();
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase, MainModelSchedule.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                Log.d("TAG7", "onBindViewHolder: ");
                String s = model.getEndTime().equals("00:00") ? "23:59" : model.getEndTime();
                LocalTime itemEndTime = LocalTime.parse(s);
                LocalTime now = LocalTime.now();
//                if (itemEndTime.isBefore(now)){
//                    holder.itemView.setVisibility(View.GONE);
//                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//                }

//                if(flag==false){
//                    flag=true;
//                    firstPosition=position; // first shown position
//                }
//
//                if (position==fbAdapter.getItemCount()-1){
//                    ViewGroup.LayoutParams params=mRecycler.getLayoutParams();
//                    int x = fbAdapter.getItemCount()-count;
//                    x = x>3 ? 3 : x;
//                    params.height=225*(x);
//                    mRecycler.setLayoutParams(params);
//                }
                sf.InitItemOfSchedule(holder,position,model); // Init each item in schedule
                //holder.button.setLayoutParams (new LinearLayout.LayoutParams(650, ViewGroup.LayoutParams.MATCH_PARENT));
                // holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
                // holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));
                if(model.getType().equals("anchor")){
                    holder.button.setTextColor(ContextCompat.getColor(getContext(), R.color.anchor));
                    holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
                    holder.button.setBackgroundResource(R.drawable.category_btn_schedule);
                    holder.button.setCompoundDrawablesWithIntrinsicBounds
                            (0, 0, R.drawable.anchor,
                                    0);
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {   //OPEN ANCHOR POPUP
                            String id = model.getAnchorID();
                            Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("AnchorKeyFromWeek", id);
                            b.putString("date",today);
                            if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                b.putString("photo", "yes");
                            }
                            else {
                                b.putString("photo", "no");
                            }
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });

                } else if (model.getType().equals("task")){
                    holder.button.setTextColor(ContextCompat.getColor(getContext(), catColor[TaskCategory.fromStringToInt(model.getCategory())]));
                    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                    holder.button.setBackgroundResource
                            (R.drawable.category_btn_schedule);
                    holder.button.setCompoundDrawablesWithIntrinsicBounds
                            (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())],
                                    0);                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = model.getActiveKey();
                            Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("TaskKeyFromWeek", id);
                            b.putString("date",today);
                            if (model.getPhotoUri()!=null && model.getPhotoUri().length()>2) {
                                b.putString("photo", "yes");
                            }
                            else {
                                b.putString("photo", "no");
                            }
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });

                }
                progressBar6.setVisibility(View.GONE);

            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("TAG7", "onCreateViewHolder: ");
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule, parent, false);
                return new MyViewHolder(v);
            }
        };
        fbAdapter.getItemCount();
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

    }

    /**
     * Show today's schedule rate if exists
     */
    private void showScheduleRate() {
        DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(today).child("data");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rate = (String) dataSnapshot.child("successful").getValue();
                if (rate.equals("yes")){
                    rateMsg.setText("You rated this schedule as successful");
                }
                else if (rate.equals("no")){
                    rateMsg.setText("You rated this schedule as unsuccessful");
                }
                else { // n/a
                    rateMsg.setText("");
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent ct;
        switch (v.getId()) {
            case (R.id.chooseTasksBtn):
                ct= new Intent(getActivity(), ChooseTasks.class);
                getActivity().startActivity(ct);
                break;
            case (R.id.rankit):
                Bundle b = new Bundle();
                Intent intent = new Intent(getActivity(), ScheduleFeedback.class);
                b.putString("date",today);
                b.putBoolean("isFromScheduleTab",false);
                intent.putExtras(b);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            default:
                break;
        }
    }

    /**
     * Toolbar creation
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu_xml, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * Toolbar click listener
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.action_logout){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * checking if personality vector is full. if not - show message
     */
    @Override
    public void checkIfPersonalityVectorFilled() {
        final ArrayList<Integer> q_answers = new ArrayList<Integer>() ;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("personality_vector");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
//                if (q_answers.get(0).equals(1)){
//                    imageView7.setImageResource(R.drawable.try2);
//                }
//                else if (q_answers.get(0).equals(2)){
//                    imageView7.setImageResource(R.drawable.try1);
//                }
                if (q_answers.contains(0)) { //questionnaire not filled
                    rankit.setVisibility(View.GONE);
                    progressBar6.setVisibility(View.GONE);
                    view11.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.GONE);
                    noSchRelative.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for today");
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("You must complete the questionnaire in order to receive schedules");
                    int answers[] = new int[25];
                    int i=0;
                    for (Integer x : q_answers){
                        answers[i++]=x;
                    }
                    Intent intent = new Intent(getActivity(),Questionnaire.class);
                    intent.putExtra("answers",answers);
                    questionnaireBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                }
                else { // questionnaire filled
                    quesIsOK();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * If personality vector is full we can show the schedule
     */
    private void quesIsOK() {
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        sf = new ScheduleFragment();
        today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(today).child("schedule");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0){ //SHOW TODAYS SCHEDULE
                    showTodaySchedule();
                }
                else {
                    ScheduleForToday.setVisibility(View.INVISIBLE);
                    hello.setVisibility(View.INVISIBLE);
                    rankit.setVisibility(View.GONE);
                    progressBar6.setVisibility(View.GONE);
                    view11.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.GONE);
                    noSchRelative.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for today");
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("Please insert tasks and press on\n"+'"'+"Build A Schedule"+'"'+" button");
                    questionnaireBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}