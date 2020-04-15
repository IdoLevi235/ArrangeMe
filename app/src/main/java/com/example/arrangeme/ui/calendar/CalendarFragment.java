package com.example.arrangeme.ui.calendar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alamkanak.weekview.WeekView;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.example.arrangeme.ui.dashboard.DashboardViewModel;

import java.util.Collection;
import java.util.HashSet;

public class CalendarFragment extends Fragment implements View.OnClickListener {

    private CalendarViewModel calendarViewModel;
    private WeekView weekCalendar;
    private Switch switchCat;
    private ConstraintLayout.LayoutParams parms;
    private ConstraintLayout.LayoutParams parms2;
    private TextView monthName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        weekCalendar = root.findViewById(R.id.weekView);
        parms = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();
        parms2 = (ConstraintLayout.LayoutParams) weekCalendar.getLayoutParams();



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
        switchCat =  view.findViewById(R.id.switchCat);
        switchCat.setOnClickListener(this);
        weekCalendar.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.switchCat):
            {
                if(switchCat.isChecked()) {
                    parms.height= (int) ((parms.height)*0.5);
                    weekCalendar.setLayoutParams(parms);
                    weekCalendar.setTranslationY(-150);
                }
                else{
                    parms.height= (int) ((parms.height)*2);
                    weekCalendar.setTranslationY(+150);
                    weekCalendar.setLayoutParams(parms);
                }
            }
            break;


            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id==R.id.action_3dots){
            Toast.makeText(getContext(), "3dots clicked inside dashboard", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO: להוסיף בסוף אייקונים לטאבים - לא מסובך אבל ארוך לשלב זה
}
