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
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class WeekFragment extends Fragment implements WeekView.OnClickListener, MonthLoader.MonthChangeListener{

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

    public WeekView getWeekCalendar() {
        return weekCalendar;
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}


