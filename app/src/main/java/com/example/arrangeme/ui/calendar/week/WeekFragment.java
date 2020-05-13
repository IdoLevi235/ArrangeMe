package com.example.arrangeme.ui.calendar.week;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.alamkanak.weekview.OnMonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WeekFragment extends Fragment implements View.OnClickListener, OnMonthChangeListener {

    private WeekView weekCalendar;
    TaskEntity task = new TaskEntity();
    List <TaskEntity> tasksFromDB = new ArrayList();
    Integer[] catIcon = {R.drawable.study, R.drawable.sport, R.drawable.work, R.drawable.nutrition, R.drawable.familycat, R.drawable.chores, R.drawable.relax, R.drawable.friends_cat, 0};
    Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = (WeekView) view.findViewById(R.id.weekView);


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
                    tasksFromDB.add(task);
                    for (int i = 0; i < tasksFromDB.size(); i++) {
                        Calendar cal = Calendar.getInstance();
                        Calendar cal3 = Calendar.getInstance();
                        try {
                            cal = DateStringToCalendar(tasksFromDB.get(i).getCreateDate());
                            cal3 = DateStringToCalendar(tasksFromDB.get(i).getCreateDate());
                            Log.d("cal", "onDataChange: "+cal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cal3.add(Calendar.HOUR,1);
                        Log.d("cal3", "onDataChange: "+cal3);
                        Event event = new Event(1, task.getCategoryS(), cal, cal3, task.getDescription(),ContextCompat.getColor(getActivity(), R.color.sport), false, false);
                        Log.d("event", "onDataChange: "+event);
                        listOfEvents.add(event);
                    }
                }
                weekCalendar.submit(listOfEvents);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        setHasOptionsMenu(true);
        return view;
    }


    public Calendar DateStringToCalendar (String date1) throws ParseException {

        String arr[] = date1.split("-");

        //TODO: SET THE HOURS
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MONTH,Integer.parseInt(arr[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[0]));
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[2]));
       return calendar;
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


