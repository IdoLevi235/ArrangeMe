package com.example.arrangeme.ui.calendar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.ForgotPass;
import com.example.arrangeme.R;
import org.w3c.dom.Text;

public class CalendarFragment extends Fragment implements View.OnClickListener{

    private CalendarViewModel calendarViewModel;
    private TextView monthName;
    private FrameLayout containerFilter;
    private FrameLayout containerCalender;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        monthName =view.findViewById(R.id.monthName);
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.filterIcon){
            openFilterFragment();
        }
        else if (id==R.id.menuDay){
            DayFragment dayfragment = new DayFragment();
            openCalendarFragment(dayfragment);
        }
        else if (id==R.id.menuWeek){
            WeekFragment weekfragment = new WeekFragment();
            openCalendarFragment(weekfragment);
        }
        else if (id==R.id.menuMonth){
            WeekFragment weekfragment = new WeekFragment();
            openCalendarFragment(weekfragment);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    private void openCalendarFragment(Fragment calenderFragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.calendars_container, calenderFragment,"Blank").commit();
    }

    private void openFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.filter_container, filterFragment,"Blank").commit();
    }


}
