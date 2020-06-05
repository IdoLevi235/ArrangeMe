package com.example.arrangeme.ui.calendar.week;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alamkanak.weekview.OnEmptyViewClickListener;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.OnMonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.arrangeme.AddAnchor;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.AnchorPagePopup;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Entities.ScheduleItem;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
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
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class WeekFragment extends Fragment implements View.OnClickListener, OnMonthChangeListener {

    private WeekView weekCalendar;
    ScheduleItem sc;
    List <ScheduleItem> scheduleFromDB = new ArrayList();

    HashMap<String, Integer> hash = new HashMap<String, Integer>();
    Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};
    String[] cat = {"study", "sport", "work", "nutrition","family", "chores", "relax", "friends", "other"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        FloatingActionButton floatingActionButton =view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        weekCalendar = (WeekView) view.findViewById(R.id.weekView);
        weekCalendar.setOnClickListener(this);
        for (int i = 0; i < catColor.length; i++) {
            hash.put(cat[i], catColor[i]);
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Schedules");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @NonNull
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot mDatabase2 = ds.child("schedule");
                    if (mDatabase2.exists()) {
                        ArrayList<HashMap<String, String>> message = (ArrayList) mDatabase2.getValue();
                        for (HashMap<String, String> entry : message) {
                            if(entry.get("type").equals("anchor")) {
                               // sc = new ScheduleItem(entry.get("AnchorID"), entry.get("type"));
                                sc = new ScheduleItem(entry.get("startTime"), entry.get("endTime"), entry.get("category"), entry.get("type"), entry.get("date"), entry.get("description"), entry.get("location"),entry.get("AnchorID"));
                            }
                            else {
                                sc = new ScheduleItem(entry.get("startTime"), entry.get("endTime"), entry.get("category"), entry.get("type"), entry.get("date"), entry.get("description"), entry.get("location"),entry.get("activeKey"));
                            }
                            scheduleFromDB.add(sc);
                        }
                    }
                }
                List<WeekViewDisplayable<Event>> listOfEvents = new ArrayList<WeekViewDisplayable<Event>>();
                for (int i = 0; i < scheduleFromDB.size(); i++) {
                    if (scheduleFromDB.get(i).getType().equals("task")) {
                        Calendar cal = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        try {
                            cal = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getStartTime());
                            cal2 = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getEndTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Event event = new Event((scheduleFromDB.get(i).getIdForCalendar()), scheduleFromDB.get(i).getDescription(), cal, cal2,scheduleFromDB.get(i).getCategory(), ContextCompat.getColor(getActivity(), hash.get(scheduleFromDB.get(i).getCategory().toLowerCase())), false, false);

                        listOfEvents.add(event);
                    }
                    else {
                        Calendar cal = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        try {
                            cal = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getStartTime());
                            cal2 = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getEndTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Event event = new Event((scheduleFromDB.get(i).getIdForCalendar()), scheduleFromDB.get(i).getDescription(), cal, cal2,scheduleFromDB.get(i).getCategory(), ContextCompat.getColor(getActivity(), R.color.anchor), false, false);

                        listOfEvents.add(event);

//                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
//                        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String anchorID = ds.getKey();
//                                    String startTime = (String) ds.child("startTime").getValue();
//                                    String endTime = (String) ds.child("endTime").getValue();
//                                    String date = (String) ds.child("date").getValue();
//                                    String description = (String) ds.child("description").getValue();
//                                    String category = (String) ds.child("category").getValue();
//
//                                    Calendar cal = Calendar.getInstance();
//                                    Calendar cal2 = Calendar.getInstance();
//                                    try {
//                                        cal = DateStringToCalendar(date, startTime);
//                                        cal2 = DateStringToCalendar(date, endTime);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    /**
//                                     * submit the anchors of the schedule in the weekview
//                                     */
//                                    Event event = new Event(anchorID, description, cal, cal2, category, ContextCompat.getColor(getActivity(), R.color.anchor), false, false);
//                                    Log.d("weekcal", "onDataChange: " + event.getId().toString());
//                                    listOfEvents.add(event);
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                            }
//                        });
                    }
                    }
                /**
                 * submit the tasks of the schedule in the weekview
                 */
                weekCalendar.submit(listOfEvents);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        /**
         * when clicking on the events , it will open their details in pop up window
         */
        weekCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onEventClick(Object o, @NotNull RectF rectF) {

                String id = ((Event) o).getId();
                String firstLetter = id.substring(0, 3);
                Log.d("weekclick", "firstLetter: "+firstLetter);
                Log.d("weekclick", "id: "+id);
                //if the first letter is "t" so it is a task
                if(firstLetter.equals("2305")){
                    Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    Bundle b = new Bundle();
                    b.putString("TaskKeyFromWeek", ((Event) o).getId());
                    intent.putExtras(b);
                    startActivity(intent);
                }
                //if the first letter isn't "t" it is an anchor
                else {
                    Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    Bundle b = new Bundle();
                    b.putString("AnchorKeyFromWeek", ((Event) o).getId());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        weekCalendar.setOnEmptyViewClickListener(new OnEmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(@NotNull Calendar calendar) {
                addAnchor();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    public void onBackPressed() {
    }


    /**
     * this function add new anchor when pressing the + or an empty cell
     */
    public void addAnchor(){
        Intent intent = new Intent(getActivity(), AddAnchor.class);
        //TODO: add anchor date like in the month
        //intent.putExtra("date", dateStringSentToAddAnchor[0]);
        startActivity(intent);
    }

    /**
     * this function convert date in string to a calendar type
     * @param date1 its the date
     * @param hour its the hour for this date
     */
    public Calendar DateStringToCalendar (String date1, String hour) throws ParseException {
        String arr[] = date1.split("-");
        String arr2[] = hour.split(":");
        Calendar calendar = Calendar.getInstance();

        if(Integer.parseInt(arr2[0])>12){
            calendar.set(Calendar.AM_PM, Calendar.PM);
            arr2[0]= String.valueOf(Integer.parseInt(arr2[0])%12);
        }
        else if(Integer.parseInt(arr2[0])<12){
            calendar.set(Calendar.AM_PM, Calendar.AM);
            if(Integer.parseInt(arr2[0])==0)
                arr2[0]= String.valueOf(24);
        }

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, Integer.parseInt(arr2[1]));
        calendar.set(Calendar.HOUR, Integer.parseInt(arr2[0]));
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
            case R.id.floatingActionButton:
                addAnchor();
            break;
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


