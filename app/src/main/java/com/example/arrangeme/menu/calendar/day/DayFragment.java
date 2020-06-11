package com.example.arrangeme.menu.calendar.day;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.OnEmptyViewClickListener;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.arrangeme.Anchors.AddAnchor;
import com.example.arrangeme.Anchors.AnchorPagePopup;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.Entities.ScheduleItem;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.tasks.TaskPagePopup;
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

public class DayFragment extends Fragment implements  View.OnClickListener{

    private WeekView dayCalendar;
    ScheduleItem sc;
    List <ScheduleItem> scheduleFromDB = new ArrayList();
    List<WeekViewDisplayable<Event>> listOfEvents = new ArrayList<>();
    HashMap<String, ScheduleItem> openPopUp = new HashMap<>();
    HashMap<String, Integer> hash = new HashMap<String, Integer>();
    Integer[] catColor = {R.color.study, R.color.sport, R.color.work, R.color.nutrition, R.color.family, R.color.chores, R.color.relax, R.color.friends, R.color.other};
    String[] cat = {"study", "sport", "work", "nutrition","family", "chores", "relax", "friends", "other"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_day, container, false);
        FloatingActionButton floatingActionButton =view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        dayCalendar = (WeekView) view.findViewById(R.id.dayView);
        dayCalendar.setOnClickListener(this);
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
                            if(entry!=null) {
                                if (entry.get("type").equals("anchor")) {

                                    sc = new ScheduleItem(entry.get("startTime"), entry.get("endTime"), entry.get("category"), entry.get("type"), entry.get("date"), entry.get("description"), entry.get("location"), entry.get("AnchorID"));
                                    ScheduleItem sc1 = new ScheduleItem(entry.get("date"), entry.get("type"), entry.get("photoUri"));
                                    openPopUp.put(sc.getIdForCalendar(), sc1);
                                } else {
                                    sc = new ScheduleItem(entry.get("startTime"), entry.get("endTime"), entry.get("category"), entry.get("type"), entry.get("date"), entry.get("description"), entry.get("location"), entry.get("activeKey"));
                                    ScheduleItem sc1 = new ScheduleItem(entry.get("date"), entry.get("type"), entry.get("photoUri"));
                                    openPopUp.put(sc.getIdForCalendar(), sc1);
                                }
                                scheduleFromDB.add(sc);
                            }
                        }
                    }
                }
                for (int i = 0; i < scheduleFromDB.size(); i++) {
                    Calendar cal = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    try {
                        cal = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getStartTime());
                        cal2 = DateStringToCalendar(scheduleFromDB.get(i).getDate(), scheduleFromDB.get(i).getEndTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (scheduleFromDB.get(i).getType().equals("task")) {
                        Event event = new Event((scheduleFromDB.get(i).getIdForCalendar()), scheduleFromDB.get(i).getDescription(), cal, cal2, scheduleFromDB.get(i).getCategory(), ContextCompat.getColor(getActivity(), hash.get(scheduleFromDB.get(i).getCategory().toLowerCase())), false, false);
                        listOfEvents.add(event);
                    }
                    else {
                        if(scheduleFromDB.get(i).getCategory()==null){
                            scheduleFromDB.get(i).setCategory(" ");
                        }
                        Event event = new Event((scheduleFromDB.get(i).getIdForCalendar()), scheduleFromDB.get(i).getDescription(), cal, cal2, scheduleFromDB.get(i).getCategory(), ContextCompat.getColor(getActivity(), R.color.anchor), false, false);
                        listOfEvents.add(event);
                    }
                }
                dayCalendar.submit(listOfEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Calendar cal = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    try {
                        Log.d("mDatabase2Week", "onDataChange: "+ds.child("date").getValue().toString());
                        cal = DateStringToCalendar(ds.child("date").getValue().toString(), ds.child("startTime").getValue().toString());
                        cal2 = DateStringToCalendar(ds.child("date").getValue().toString(), ds.child("endTime").getValue().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(!ds.hasChild("category")){
                        Event event = new Event((ds.getKey()), ds.child("description").getValue().toString(), cal, cal2, " ", ContextCompat.getColor(getActivity(), R.color.anchor), false, false);
                        if(ds.hasChild("photoUri")) {
                            ScheduleItem sc1 = new ScheduleItem(ds.child("date").getValue().toString(), "anchor", ds.child("photoUri").getValue().toString());
                            openPopUp.put(ds.getKey(),sc1);
                        }
                        else {
                            ScheduleItem sc1 = new ScheduleItem(ds.child("date").getValue().toString(), "anchor", " ");
                            openPopUp.put(ds.getKey(),sc1);
                        }

                        listOfEvents.add(event);
                    }
                    else {
                        Event event = new Event((ds.getKey()), ds.child("description").getValue().toString(), cal, cal2, ds.child("category").getValue().toString(), ContextCompat.getColor(getActivity(), R.color.anchor), false, false);
                        if(ds.hasChild("photoUri")) {
                            ScheduleItem sc1 = new ScheduleItem(ds.child("date").getValue().toString(), "anchor", ds.child("photoUri").getValue().toString());
                            openPopUp.put(ds.getKey(),sc1);
                        }
                        else {
                            ScheduleItem sc1 = new ScheduleItem(ds.child("date").getValue().toString(), "anchor", " ");
                            openPopUp.put(ds.getKey(),sc1);
                        }

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
                String id = ((Event) o).getId();

                if(openPopUp.get(id).getType().equals("task")) {
                    if(openPopUp.get(id).getPhotoUri()!=null&&openPopUp.get(id).getPhotoUri().length()>2) {
                        Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Bundle b = new Bundle();
                        b.putString("TaskKeyFromWeek", ((Event) o).getId());
                        b.putString("date", openPopUp.get(id).getDate());
                        b.putString("photo", "yes");
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getActivity(), TaskPagePopup.class);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Bundle b = new Bundle();
                        b.putString("TaskKeyFromWeek", ((Event) o).getId());
                        b.putString("date", openPopUp.get(id).getDate());
                        b.putString("photo", "no");
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
                else {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors").child(id);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild("photoUri")) {
                                Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("AnchorKeyFromWeek", ((Event) o).getId());
                                b.putString("date",openPopUp.get(id).getDate());
                                b.putString("photo", "yes");
                                intent.putExtras(b);
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(getActivity(), AnchorPagePopup.class);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                Bundle b = new Bundle();
                                b.putString("AnchorKeyFromWeek", ((Event) o).getId());
                                b.putString("date",openPopUp.get(id).getDate());
                                b.putString("photo", "no");
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        });


        dayCalendar.setOnEmptyViewClickListener(new OnEmptyViewClickListener() {
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
        else if(Integer.parseInt(arr2[0])<=12){
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
        dayCalendar.setOnClickListener(this);
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

}

