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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //this.checkIfPersonalityVectorFilled();
        //this.checkIfThereIsSchedule();
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        ScheduleFragment sf = new ScheduleFragment();
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        /*mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("tasks").child("Pending_tasks");
       // Query query = mDatabase.orderByChild("createDate").equalTo(today);
        options = new FirebaseRecyclerOptions.Builder<MainModelSchedule>().setQuery(mDatabase, MainModelSchedule.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelSchedule, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModelSchedule model) {
                Log.d("TAG7", "onBindViewHolder: ");
                sf.InitItemOfSchedule(holder,position,model); // Init each item in schedule
                holder.button.setLayoutParams (new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(90, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));

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
        */
        }

    public void checkIfThereIsSchedule() {

        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("task").child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d("TAG7", "onDataChange: " + dataSnapshot);
                    if ((dataSnapshot.getChildrenCount()==0)) {
                        view11.setVisibility(View.GONE);
                        mRecycler.setVisibility(View.GONE);
                        noSchRelative.setVisibility(View.VISIBLE);
                        noScheduleYet.setVisibility(View.VISIBLE);
                        noScheduleYet.setText("You don't have schedule for today");
                        quesMessage.setVisibility(View.VISIBLE);
                        quesMessage.setText("Please insert tasks and press red the button to build a schedule");
                        questionnaireBtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


   /* public void InitItemOfSchedule(MyViewHolder holder, int position, MainModelSchedule model) {
        Log.d("TAG7", "InitItemOfSchedule: ");
        holder.timeText.setText(" "+model.getTime());
        holder.button.setText("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
        SpannableStringBuilder str = new SpannableStringBuilder
                ("\t"+model.getCategory()+" \n\n\t"+model.getDescription());
        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, model.getCategory().length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.05f), 0, model.getCategory().length()+1, 0);
        holder.button.setText(str);
        holder.button.setLayoutParams (new LinearLayout.LayoutParams(720, ViewGroup.LayoutParams.MATCH_PARENT));
        holder.timeText.setLayoutParams (new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
        holder.anchorOrTask.setLayoutParams (new LinearLayout.LayoutParams(80, 76));
        if(model.getType().equals("ANCHOR")) {
            holder.anchorOrTask.setBackgroundResource(R.drawable.try_anchor_time);
        }
        else if (model.getType().equals("TASK"))
            holder.anchorOrTask.setBackgroundResource(R.drawable.task_time);
        holder.button.setBackgroundResource
                (catBackgroundFull[TaskCategory.fromStringToInt(model.getCategory())]);
        holder.button.setCompoundDrawablesWithIntrinsicBounds
                (0,0,catIcon[TaskCategory.fromStringToInt(model.getCategory())],0);

    }*/


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
                Intent intent = new Intent(getActivity(), ScheduleFeedback.class);
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
            Toast.makeText(getContext(), "Logout clicked inside dashboard", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void checkIfPersonalityVectorFilled() {
        final ArrayList<Integer> q_answers = new ArrayList<Integer>() ;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("personality_vector");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!(data.getKey().equals("0"))) { // ignore children 0 of "Personality vector" (doesn't exist (null), only 1-->25)
                        q_answers.add(Integer.parseInt(data.getValue().toString()));
                    }
                }
                if (q_answers.contains(0)) {
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
