package com.example.arrangeme.ui.calendar.week;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.OnEmptyViewClickListener;
import com.alamkanak.weekview.OnMonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class WeekFragment extends Fragment implements WeekView.OnTouchListener, WeekView.OnClickListener, OnMonthChangeListener<WeekViewEvent>  {

    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private RecyclerView eventsRecyclerView;
    private ArrayList<WeekViewEvent> mNewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = (WeekView) view.findViewById(R.id.weekView);
        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        // Set an action when any event is clicked.
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchCat =  view.findViewById(R.id.switchCat);
        relativeLayout =  view.findViewById(R.id.relativeLayout);
        eventsName =  view.findViewById(R.id.eventsName);
        eventsRecyclerView =  view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setOnClickListener(this);
        switchCat.setOnClickListener(this);
        weekCalendar.setOnClickListener(this);
        weekCalendar.setEmptyViewClickListener(new OnEmptyViewClickListener() {
         @Override
         public void onEmptyViewClicked(Calendar calendar) {
           popupTaskorAnchor();
         }
        });
        setupDateTimeInterpreter(false);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        weekCalendar.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public void onSetNumberOfDays(int i) {

            }

            @Override
                public String interpretDate(Calendar date) {
                    SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                    String weekday = weekdayNameFormat.format(date.getTime());
                    SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                    // All android api level do not have a standard way of getting the first letter of
                    // the week day name. Hence we get the first char programmatically.
                    // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                    if (shortDate)
                        weekday = String.valueOf(weekday.charAt(0));
                    return weekday.toUpperCase() + format.format(date.getTime());
                }

                @Override
                public String interpretTime(int hour) {
                    return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
                }
            });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.switchCat):
            {
                if(switchCat.isChecked()) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    parms.height= (int) ((parms.height)*(0.64));
                    weekCalendar.setLayoutParams(parms);

                }
                else{
                    relativeLayout.setVisibility(View.GONE);
                    parms.height= (int) ((parms.height)*(1.5625));
                    weekCalendar.setLayoutParams(parms);

                }
            }
            break;
            case (R.id.weekView):


                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    //pop up to ask if the user want to create task/anchor
    private void popupTaskorAnchor() {
        SweetAlertDialog ad;
        ad = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE).setContentText(("Do you want to add a task or an anchor?"));
        ad.setConfirmText("Task");
        ad.setCancelText("Anchor");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(getActivity(), AddTasks.class));
            }
        });
        ad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Toast.makeText(getContext(), "Achor pressed", Toast.LENGTH_LONG).show();
            }
        });
        ad.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @Override
    public List<WeekViewDisplayable<WeekViewEvent>> onMonthChange(Calendar newYear, Calendar newMonth) {
        final List events = new ArrayList();
        return events;
    }
    }


