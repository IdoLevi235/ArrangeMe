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

    private WeekView<Event> weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_week, container, false);
        weekCalendar = (WeekView<Event>) view.findViewById(R.id.weekView);

        weekCalendar.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NotNull Calendar calendar, @NotNull Calendar calendar1) {
                Calendar cal = Calendar.getInstance();
                Calendar cal2 =cal;
                cal2.add(Calendar.HOUR_OF_DAY, 1);
                Log.d("cal", "onload"+cal.toString());
                Log.d("cal", "onload"+cal2.toString());
                Event event = new Event(523,"Family",cal,cal2,"here",R.color.com_facebook_blue,true,false);
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                cal2 =cal;
                cal2.add(Calendar.DATE, 2);
                Log.d("cal", "onload"+cal.toString());
                Log.d("cal", "onload"+cal2.toString());
                Event event2 = new Event(523,"Family",cal,cal2,"here",R.color.com_facebook_blue,true,false);

                List<WeekViewDisplayable<Event>> list = new ArrayList<WeekViewDisplayable<Event>>();
                list.add(event);
                list.add(event2);
                weekCalendar.submit(list);
            }
        });
        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchCat =  view.findViewById(R.id.switchCat);
        switchCat.setOnClickListener(this);
        Button button2= view.findViewById(R.id.button2);
        button2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.button2):
            {
                startActivity(new Intent(getActivity(), Main2Activity.class));
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    public List<WeekViewDisplayable<Event>> onMonthChange(@NonNull Calendar startDate, @NonNull Calendar endDate) {

        //check if it loads event
        Calendar cal = Calendar.getInstance();
        Calendar cal2 =cal;
        cal2.add(Calendar.HOUR_OF_DAY, 1);
        Event event = new Event(523,"Family",cal,cal2,"here",R.color.com_facebook_blue,true,false);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal2 =cal;
        cal2.add(Calendar.DATE, 2);
        Event event2 = new Event(523,"Family",cal,cal2,"here",R.color.com_facebook_blue,true,false);

        List<WeekViewDisplayable<Event>> list = new ArrayList<WeekViewDisplayable<Event>>();
        list.add(event);
        list.add(event2);

        return list;
        //weekCalendar.submit(list);

        // return database.getEventsInRange(startDate, endDate);
    }


}


