package com.example.arrangeme;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.ui.calendar.CalendarFragment;
import com.example.arrangeme.ui.calendar.FilterFragment;
import com.example.arrangeme.ui.schedule.ScheduleFragment;
import com.example.arrangeme.ui.tasks.TasksFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Home page class - the main page of the application. has bottom navigation bar with 5 fragments
 */
public class Homepage extends AppCompatActivity{

    private Toolbar toolbar;
    private int value;
    public static String filter;
    private Button node ;
    private FirebaseFunctions mFunctions;
    private String dateToShowInScheduleFragment;
    public static Context contextOfApplication;

    @SuppressLint("ResourceType")
    @Override

    /**
     * this function controls what happens on creation of the activity
     * @param savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
       // node=findViewById(R.id.nodeBtn);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                getSupportFragmentManager().popBackStack();
                return false;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar_homepage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_dashboard, R.id.navigation_calendar, R.id.navigation_tasks, R.id.navigation_schedule, R.id.navigation_myprofile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent i = getIntent();
        String data = i.getStringExtra("FromHomepage");
        String date = i.getStringExtra("date");

        Log.d("TAG1", "onCreate: in homepage" + date);
        if (data != null && data.contentEquals("1")) {
            navView.setSelectedItemId(R.id.navigation_tasks);
        }
        else if(data != null && data.contentEquals("2")){
            navView.setSelectedItemId(R.id.navigation_calendar);
        }
        else if(data != null && data.contentEquals("3")){
            if (date!=null)
                {
                    dateToShowInScheduleFragment = date;
                }
            navView.setSelectedItemId(R.id.navigation_schedule);
        }

        else if(data != null && data.contentEquals("5")){
            navView.setSelectedItemId(R.id.navigation_myprofile);
        }
        contextOfApplication = getApplicationContext();



    }

    /**
     * Thie function returns the context
     * @return Context
     */
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    /**
     * Inflates the correct toolbar menu
     * @param menu
     * @return boolean true/false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }


    /**
     * Controls what happens when menu item is pressed
     * @param item
     * @return boolean true/false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Homepage.this, MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }



    /**
     * overriding the back button of the cellphone and adding few conditions
     */
    @Override
    public void onBackPressed() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (navView.getSelectedItemId()!=R.id.navigation_dashboard) {
                super.onBackPressed();
            }
            else {
                //do nothing for now
            }

        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


    /**
     * dateToShowInScheduleFragment is a Variable that assists showing
     * correct date in schedule tab after user chooses tasks for a certain day
     *
     * this functions returns current value of this variable
     * @return  dateToShowInScheduleFragment
     */
    public String getDateToShowInScheduleFragment() {
        return dateToShowInScheduleFragment;
    }


    /**
     * dateToShowInScheduleFragment is a Variable that assists showing
     * correct date in schedule tab after user chooses tasks for a certain day
     *
     * this functions sets current value of this variable
     */

    public void setDateToShowInScheduleFragment(String dateToShowInScheduleFragment) {
        this.dateToShowInScheduleFragment = dateToShowInScheduleFragment;
    }

}
