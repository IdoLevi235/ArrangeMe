package com.example.arrangeme.ui.calendar.week;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alamkanak.weekview.OnEmptyViewLongClickListener;
import com.alamkanak.weekview.OnEventClickListener;
import com.alamkanak.weekview.OnEventLongClickListener;
import com.alamkanak.weekview.OnLoadMoreListener;
import com.alamkanak.weekview.OnMonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.arrangeme.Entities.Event;
import com.example.arrangeme.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements OnEventClickListener<Event>, OnMonthChangeListener<Event>, OnEventLongClickListener<Event>, OnEmptyViewLongClickListener {

    //private EventsDatabase database;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        WeekView<Event> weekView = findViewById(R.id.weekView);
      //  Toolbar toolbar = findViewById(R.id.toolbar);
       // ToolbarUtils.setupWithWeekView(toolbar, weekView);
        //database = new EventsDatabase(this);

        weekView.setOnEventClickListener(this);
        weekView.setOnMonthChangeListener(this);
        weekView.setOnEventLongClickListener(this);
        weekView.setOnEmptyViewLongClickListener(this);
        weekView.setOnLoadMoreListener(new OnLoadMoreListener() {
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
                weekView.submit(list);
            }
        });
    }

    @NotNull
    @Override
    public List<WeekViewDisplayable<Event>> onMonthChange(@NonNull Calendar startDate, @NonNull Calendar endDate) {

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

        return list;
        //weekCalendar.submit(list);

       // return database.getEventsInRange(startDate, endDate);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onEventClick(@NonNull Event event, @NonNull RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getTitle(),1).show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onEventLongClick(@NonNull Event event, @NonNull RectF eventRect) {
        Toast.makeText(this, "Long-clicked event: " + event.getTitle(),1).show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onEmptyViewLongClick(@NonNull Calendar time) {

        Toast.makeText(this, "Empty view long pressed:",1).show();
    }
}
