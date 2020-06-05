package com.example.arrangeme.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AnchorPagePopup;
import com.example.arrangeme.ChooseTasks.ChooseTasks;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.ScheduleFeedback;
import com.example.arrangeme.ui.schedule.MainModelSchedule;
import com.example.arrangeme.ui.schedule.MyViewHolder;
import com.example.arrangeme.PersonalityVectorValidate;
import com.example.arrangeme.ui.schedule.ScheduleFragment;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment implements View.OnClickListener, PersonalityVectorValidate {

    private DashboardViewModel dashboardViewModel;
    private ScheduleFragment sf ;
    private String today;
    private LinearLayoutManager layoutManager;
    private Button chooseTasksBtn;
    private Button questionnaireBtn;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private FirebaseRecyclerOptions<MainModelSchedule> options;
    private FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder> fbAdapter;
    private RelativeLayout noSchRelative;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private TextView welcome;
    private TextView rateMsg;
    private RelativeLayout view11;
    private AppCompatImageView rankit;
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
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke, R.drawable.rounded_rec_other_nostroke};
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String UID;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        rateMsg=view.findViewById(R.id.rateMsg);
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
        rankit = view.findViewById(R.id.rankit);
        rankit.setOnClickListener(this);
        view11=view.findViewById(R.id.view11);
        view11.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        this.checkIfPersonalityVectorFilled();
        }

    private void showTodaySchedule() {
        showScheduleRate();
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase, MainModelSchedule.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                Log.d("TAG7", "onBindViewHolder: ");
                sf.InitItemOfSchedule(holder,position,model); // Init each item in schedule
                holder.button.setLayoutParams (new LinearLayout.LayoutParams(650, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));
                if(model.getType().equals("anchor")){
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {   //OPEN ANCHOR POPUP
                        String id = model.getAnchorID();
                        Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Bundle b = new Bundle();
                        b.putString("AnchorKeyFromWeek", id);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                } else if (model.getType().equals("task")){
                    holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
                    holder.button.setBackgroundResource
                            (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
                    holder.button.setCompoundDrawablesWithIntrinsicBounds
                            (0, 0, catIcon[TaskCategory.fromStringToInt(model.getCategory())],
                                    0);
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = model.getActiveKey();
                            Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("TaskKeyFromWeek", id);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });

                }
        }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("TAG7", "onCreateViewHolder: ");
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule, parent, false);
                return new MyViewHolder(v);
            }

        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);

    }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu_xml, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
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
                if (q_answers.contains(0)) { //questionnaire not filled
                    view11.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.GONE);
                    noSchRelative.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for today");
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("You must complete the questionnaire in order to receive schedules!");

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
                    view11.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.GONE);
                    noSchRelative.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for today");
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("Please insert tasks and press on\n'Build A Schedule' button!");
                    questionnaireBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
