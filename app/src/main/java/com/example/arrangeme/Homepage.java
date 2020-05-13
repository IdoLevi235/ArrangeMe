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
import android.widget.Toast;

import com.example.arrangeme.AddTasks.AddTasks;
import com.example.arrangeme.Questionnaire.Questionnaire;
import com.example.arrangeme.ui.calendar.CalendarFragment;
import com.example.arrangeme.ui.calendar.FilterFragment;
import com.example.arrangeme.ui.tasks.TasksFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

public class Homepage extends AppCompatActivity {
private Toolbar toolbar;
private int value;
public static String filter;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        toolbar.setNavigationIcon(R.drawable.backsmall);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_dashboard, R.id.navigation_calendar, R.id.navigation_tasks, R.id.navigation_schedule, R.id.navigation_myprofile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent i = getIntent();
        String data = i.getStringExtra("FromHomepage");
        if (data != null && data.contentEquals("1")) {
            navView.setSelectedItemId(R.id.navigation_tasks);

        }
        else if(data != null && data.contentEquals("2")){
            navView.setSelectedItemId(R.id.navigation_calendar);
        }
        else if(data != null && data.contentEquals("5")){
            navView.setSelectedItemId(R.id.navigation_myprofile);
        }
        contextOfApplication = getApplicationContext();

        // Notifications stuff //
        String id = "notifyQUes";
        CharSequence name = "QuestionnaireNotfilledNotification";
        String description = "Channel to notify on unfilled questionnaires";
        createNotificationChannel(id,name,description);
        Calendar calendar = Calendar.getInstance();

        Intent intent = new Intent(getApplicationContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        // Notifications stuff end //



    }

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private void createNotificationChannel(String id, CharSequence name, String description) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }


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


}
