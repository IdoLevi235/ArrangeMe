package com.example.arrangeme.ui.calendar;

import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.OnEmptyViewClickListener;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent.Style.Builder;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.R;

import java.util.Calendar;
import java.util.List;

public class WeekFragment extends Fragment implements View.OnClickListener, WeekViewDisplayable<TaskEntity> {

    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private RecyclerView eventsRecyclerView;


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
        //weekCalendar.setEmptyViewClickListener(new OnEmptyViewClickListener() {
       //     @Override
       //     public void onEmptyViewClicked(Calendar calendar) {
       //         //TODO: insert a popup "do you want to create an Event? choose:" Anchor , task
            }
       // });
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


    @Override
    public WeekViewEvent toWeekViewEvent() {

        return null;
    }
}
