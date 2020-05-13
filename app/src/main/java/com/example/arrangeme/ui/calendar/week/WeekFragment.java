package com.example.arrangeme.ui.calendar.week;

import android.content.Intent;
import android.graphics.RectF;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class WeekFragment extends Fragment implements View.OnClickListener {

    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = (WeekView) view.findViewById(R.id.weekView);
        Calendar cal = Calendar.getInstance();
        Calendar cal2 =Calendar.getInstance();
        Calendar cal3 =Calendar.getInstance();
        Calendar cal4=Calendar.getInstance();

        cal.add(Calendar.HOUR_OF_DAY,6);
        cal2.add(Calendar.HOUR_OF_DAY,9);

        cal3.set(2020,5,13,1,0);
        cal4.set(2020,5,13,2,0);
        

        Event event1 = new Event(
                523,
                "Family",
                cal,
                cal2,
                "here",
                R.color.family,
                false,
                false);

        Event event2 = new Event(
                523,
                "Sport",
                cal3,
                cal4,
                "here",
                R.color.sport,
                false,
                false);


        List<WeekViewDisplayable<Event>> list = new ArrayList<WeekViewDisplayable<Event>>();
        list.add(event1);
        list.add(event2);
        weekCalendar.submit(list);
        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        setHasOptionsMenu(true);
        return view;
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


}


