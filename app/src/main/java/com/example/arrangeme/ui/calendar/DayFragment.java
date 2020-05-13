package com.example.arrangeme.ui.calendar;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.OnEmptyViewClickListener;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.arrangeme.AddAnchor;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.AnchorPagePopup;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.tasks.TaskPagePopup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DayFragment extends Fragment implements  View.OnClickListener{

    private WeekView dayCalendar;
    TaskEntity task = new TaskEntity();
    List<TaskEntity> tasksFromDB = new ArrayList();

    HashMap<String, Integer> hash = new HashMap<String, Integer>();
    Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};
    String[] cat = {"study", "sport", "work", "nutrition","family", "chores", "relax", "friends", "other"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_day, container, false);
        dayCalendar = view.findViewById(R.id.dayView);
        FloatingActionButton floatingActionButton =view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        dayCalendar.setOnClickListener(this);

        for (int i = 0; i < catColor.length; i++) {
            hash.put(cat[i], catColor[i]);
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WeekViewDisplayable<Event>> listOfEvents = new ArrayList<WeekViewDisplayable<Event>>();
                // TODO:after database with dates: we need to change it to task all the tasks in this month, month after and month before, and if it works to copy it to ONMOTHCHANGE function.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    task.setDescription((String) ds.child("description").getValue());
                    task.setLocation((String) ds.child("location").getValue());
                    task.setCategoryS((String) ds.child("category").getValue());
                    task.setCreateDate((String) ds.child("createDate").getValue());
                    task.setReminderTypeS((String) ds.child("reminderType").getValue());
                    task.setId(ds.getKey());
                    tasksFromDB.add(task);
                    for (int i = 0; i < tasksFromDB.size(); i++) {
                        Calendar cal = Calendar.getInstance();
                        Calendar cal3 = Calendar.getInstance();
                        try {
                            cal = DateStringToCalendar(tasksFromDB.get(i).getCreateDate());
                            cal3 = DateStringToCalendar(tasksFromDB.get(i).getCreateDate());
                            Log.d("cal", "onDataChange: " + cal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //TODO: change the cal3- for the finish date
                        cal3.add(Calendar.HOUR, 3);
                        Event event = new Event(task.getId(),  task.getDescription(), cal, cal3,task.getCategoryS() , ContextCompat.getColor(getActivity(), hash.get(task.getCategoryS().toLowerCase())), false, false);
                        listOfEvents.add(event);
                    }
                }
                dayCalendar.submit(listOfEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        dayCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onEventClick(Object o, @NotNull RectF rectF) {
                String id = ((Event)o).getId();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks").child(id);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("type").getValue().equals("TASK")) {
                            Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("TaskKey", ((Event)o).getId());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        else if (dataSnapshot.child("type").getValue().equals("ANCHOR")){
                            Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            Bundle b = new Bundle();
                            b.putString("AnchorKey", ((Event)o).getId());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        dayCalendar.setOnEmptyViewClickListener(new OnEmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(@NotNull Calendar calendar) {
                addTaskOrAnchor();
            }
        });

        setHasOptionsMenu(true);
        return view;


    }

    public Calendar DateStringToCalendar(String createDate) throws ParseException{
        String arr[] = createDate.split("-");

        //TODO: SET THE HOURS
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.MONTH,Integer.parseInt(arr[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[0]));
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[2]));
        return calendar;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayCalendar.setOnClickListener(this);
    }

    public void addTaskOrAnchor(){
        SweetAlertDialog ad=  new SweetAlertDialog( getActivity(), SweetAlertDialog.NORMAL_TYPE).setContentText(("Do you want to add a task or an anchor?"));
        ad.setConfirmText("Task");
        ad.setCancelText("Anchor");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(getActivity(), AddTasks.class));
            }
        });
        Intent intent = new Intent(getActivity(), AddAnchor.class);
        ad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                //TODO: add the same date as the square, don't know if possible
                startActivity(new Intent(getActivity(), AddAnchor.class));
            }
        });
        ad.show();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.floatingActionButton:
                addTaskOrAnchor();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}

