package com.example.arrangeme.ui.calendar.week;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.OnEmptyViewLongClickListener;
import com.alamkanak.weekview.OnLoadMoreListener;
import com.alamkanak.weekview.OnMonthChangeListener;
import com.alamkanak.weekview.WeekView;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Enums.ReminderType;
import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.ForgotPass;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Login;
import com.example.arrangeme.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import org.jetbrains.annotations.NotNull;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.OnEventLongClickListener;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class WeekFragment extends Fragment implements View.OnClickListener, OnMonthChangeListener {

    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;
    TaskEntity task = new TaskEntity();
    private TaskCategory taskCategory;
    List <TaskEntity> tasksFromDB = new ArrayList();
    Integer[] catIcon = {R.drawable.study, R.drawable.sport, R.drawable.work, R.drawable.nutrition, R.drawable.familycat, R.drawable.chores, R.drawable.relax, R.drawable.friends_cat, 0};
    Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = (WeekView) view.findViewById(R.id.weekView);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 =Calendar.getInstance();
        Calendar cal4=Calendar.getInstance();

        cal1.add(Calendar.HOUR_OF_DAY,6);
        cal2.add(Calendar.HOUR_OF_DAY,9);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // TODO:it takes all the tasks but, we need to change it to task all the tasks in this month, month after and month before, and if it works to copy it to ONMOTHCHANGE function.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    task.setDescription((String) ds.child("description").getValue());
                    task.setLocation((String) ds.child("location").getValue());
                    task.setCategoryS((String) ds.child("category").getValue());
                    task.setCreateDate((String) ds.child("createDate").getValue());
                    task.setReminderTypeS((String) ds.child("reminderType").getValue());
                    tasksFromDB.add(task);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        for(int i=0;i<tasksFromDB.size();i++) {
            Calendar cal = null;
            try {
                cal = DateStringToCalendar(tasksFromDB.get(i).getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal3 = cal;

            cal3.add(Calendar.HOUR, 1);
            
            Event event = new Event(1,tasksFromDB.get(i).getCategoryS(),cal,cal3,tasksFromDB.get(i).getLocation(),R.color.sport,false,false);
            List<WeekViewDisplayable<Event>> listOfEvents = new ArrayList<WeekViewDisplayable<Event>>();
            listOfEvents.add(event);
            weekCalendar.submit(listOfEvents);
        }



        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        setHasOptionsMenu(true);
        return view;
    }

    public Calendar DateStringToCalendar (String date1) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = df.parse(date1);
        Calendar cal = Calendar.getInstance();
        //or Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }


    @NotNull
    @Override
    public List<WeekViewDisplayable> onMonthChange(@NotNull Calendar calendar, @NotNull Calendar calendar1) {


        return null;
    }
}


