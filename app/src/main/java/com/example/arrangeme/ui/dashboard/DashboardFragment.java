package com.example.arrangeme.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.ChooseTasks.ChooseTasks;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.schedule.MainModelSchedule;
import com.example.arrangeme.ui.schedule.MyViewHolder;
import com.example.arrangeme.PersonalityVectorValidate;
import com.example.arrangeme.ui.schedule.ScheduleFragment;
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
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment implements View.OnClickListener, PersonalityVectorValidate {

    private DashboardViewModel dashboardViewModel;
    private Button calenderBtn;
    private Button addTaskBtn;
    private Button chooseTasksBtn;
    private Button questionnaireBtn;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModelDashboard> options;
    private FirebaseRecyclerAdapter<MainModelDashboard, MyViewHolderDashboard> fbAdapter;
    private RelativeLayout noSchRelative;
    private TextView noScheduleYet;
    private TextView quesMessage;
    private RecyclerView mRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Here we need to change the text view of the name and more staff to change
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calenderBtn = view.findViewById(R.id.calenderBtn);
        addTaskBtn = view.findViewById(R.id.addTaskBtn);
        chooseTasksBtn = view.findViewById(R.id.chooseTasksBtn);
        chooseTasksBtn.setOnClickListener(this);
        addTaskBtn.setOnClickListener(this);
        calenderBtn.setOnClickListener(this);
        quesMessage = view.findViewById(R.id.quesMessage);
        noScheduleYet = view.findViewById(R.id.noScheduleYet);
        noSchRelative = view.findViewById(R.id.noScheduleLayout);
        noSchRelative.setVisibility(View.GONE);
        questionnaireBtn = view.findViewById(R.id.questionnaireBtn);
        questionnaireBtn.setOnClickListener(this);
        mRecycler=view.findViewById(R.id.recyclerDash);
        this.checkIfPersonalityVectorFilled();
        ScheduleFragment sf = new ScheduleFragment();
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        Query query = mDatabase.orderByChild("createDate").equalTo(today);
        options = new FirebaseRecyclerOptions.Builder<MainModelDashboard>().setQuery(query, MainModelDashboard.class).build();
        fbAdapter = new FirebaseRecyclerAdapter<MainModelDashboard, MyViewHolderDashboard>(options) {

            @NonNull
            @Override
            public MyViewHolderDashboard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("TAG7", "onCreateViewHolder: ");
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule, parent, false);
                return new MyViewHolderDashboard(v);
            }

            @SuppressLint({"WrongConstant", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderDashboard holder, int position, @NonNull MainModelDashboard model) {
                Log.d("TAG7", "onBindViewHolder: ");
            }
        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);
        }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent ct;
        switch (v.getId()) {
            case (R.id.calenderBtn):
                ct = new Intent(getActivity(), Homepage.class);
                ct.putExtra("FromHomepage", "2");
                getActivity().startActivity(ct);
                break;
            case (R.id.addTaskBtn):
                 ct= new Intent(getActivity(), AddTasks.class);
                getActivity().startActivity(ct);
                break;
            case (R.id.chooseTasksBtn):
                 ct= new Intent(getActivity(), ChooseTasks.class);
                getActivity().startActivity(ct);
                break;
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
                    Log.d("TAG7", "onDataChange: CONTAIN0" );
                    mRecycler.setVisibility(View.GONE);
                    noSchRelative.setVisibility(View.VISIBLE);
                    noScheduleYet.setVisibility(View.VISIBLE);
                    noScheduleYet.setText("You don't have schedule for today");
                    quesMessage.setVisibility(View.VISIBLE);
                    quesMessage.setText("You must complete the questionnaire in order to receive schedules!");
                    questionnaireBtn.setVisibility(View.VISIBLE);
                    questionnaireBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), Questionnaire.class)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
