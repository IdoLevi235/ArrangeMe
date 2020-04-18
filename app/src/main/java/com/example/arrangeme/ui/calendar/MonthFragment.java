package com.example.arrangeme.ui.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.R;

import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;


public class MonthFragment extends Fragment implements  View.OnClickListener  {


    private CalendarView monthCalendar;
    private Switch switchCat;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private RecyclerView eventsRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_month, container, false);
        monthCalendar = view.findViewById(R.id.monthCalendar);
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
        monthCalendar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.switchCat):
            {
                if(switchCat.isChecked()) {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                else{
                    relativeLayout.setVisibility(View.GONE);
                }
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}


