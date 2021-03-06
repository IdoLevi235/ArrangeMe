package com.example.arrangeme.menu.calendar;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.menu.calendar.day.DayFragment;
import com.example.arrangeme.menu.calendar.month.MonthFragment;
import com.example.arrangeme.menu.calendar.week.WeekFragment;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * class that controls the main calendar fragment
 */
public class CalendarFragment extends Fragment implements View.OnClickListener{

    private CalendarViewModel calendarViewModel;
    private TextView monthName;
    private FrameLayout containerFilter;
    private FrameLayout containerCalender;
    private Button DayBtn;
    private Button WeekBtn;
    private Button MonthBtn;
    public static int flag=0;


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        containerFilter = root.findViewById(R.id.filter_container);
        containerCalender = root.findViewById(R.id.calendars_container);

        //Default Schedule is Week View
        WeekFragment weekfragment = new WeekFragment();
        openCalendarFragment(weekfragment);

        calendarViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override

            public void onChanged(@Nullable String s) {
                //Here we need to change the text view of the name and more staff to change
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        monthName =view.findViewById(R.id.monthName);
        DayBtn =view.findViewById(R.id.DayBtn);
        WeekBtn =view.findViewById(R.id.WeekBtn);
        MonthBtn =view.findViewById(R.id.MonthBtn);
        DayBtn.setOnClickListener(this);
        WeekBtn.setOnClickListener(this);
        MonthBtn.setOnClickListener(this);
        Globals.setFocus2(DayBtn,MonthBtn ,WeekBtn);

    }

    /**
     * @param v
     */
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.DayBtn):
                Globals.setFocus2(WeekBtn,MonthBtn ,DayBtn);
                DayFragment dayfragment = new DayFragment();
                openCalendarFragment(dayfragment);
                break;
            case (R.id.WeekBtn):
                Globals.setFocus2(DayBtn,MonthBtn ,WeekBtn);
                WeekFragment weekfragment = new WeekFragment();
                openCalendarFragment(weekfragment);
                break;
            case (R.id.MonthBtn):
                Globals.setFocus2(WeekBtn,DayBtn ,MonthBtn);
                MonthFragment monthFragment = new MonthFragment();
                openCalendarFragment(monthFragment);
                break;
            default:
                break;
        }

    }

    public void callParentMethod(){
        getActivity().onBackPressed();
    }


    /**
     * Toolbar creation
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Toolbar Item selected listener
     * @param item
     * @return
     */
    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.filterIcon){
            if (flag==0)
                openFilterFragment();
            else if(flag==1){
            closeFilterFragment();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Closing filter
     */
    private void closeFilterFragment() {
        flag = 0;
        //getActivity().getSupportFragmentManager().popBackStack();
        }

    /**
     * Open filter
     */
    private void openFilterFragment() {
        flag=1;
        FilterFragment filterFragment = new FilterFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.filter_container, filterFragment,"Blank").commit();
    }


    /**
     * @param calenderFragment
     */
    @SuppressLint("ResourceType")
    private void openCalendarFragment(Fragment calenderFragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.calendars_container, calenderFragment,"Blank").commit();
    }
}

