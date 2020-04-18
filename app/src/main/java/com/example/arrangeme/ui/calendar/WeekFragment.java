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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment implements View.OnClickListener{

    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;
    private RelativeLayout relativeLayout;
    private TextView eventsName;
    private RecyclerView eventsRecyclerView;


    public WeekFragment() {
        // Required empty public constructor
    }

    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = root.findViewById(R.id.weekView);
        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        return root;
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
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

}
