package com.example.arrangeme.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.example.arrangeme.StartScreens.MainActivity;
import com.example.arrangeme.menu.dashboard.DashboardFragment;
import com.example.arrangeme.menu.tasks.TaskPagePopup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

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
            tutorial();
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


        //This is the call for the help system
        tutorial();

    }

    private void tutorial() {
        // if(Globals.isNewUser==true) {
        if(Globals.tutorial==0) {
            Globals.tutorial++;
            new MaterialTapTargetPrompt.Builder(this).setTextGravity(Gravity.CENTER).setTarget(R.id.navigation_tasks).setPrimaryText("Hi! Click to add your first task").setSecondaryText("In order to build a schedule you need to add new tasks.").setBackgroundColour(Color.parseColor("#20666E")).show();
        }



        // }
    }

    /**
     * The function returns the context
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
                if(navView.getSelectedItemId()==R.id.navigation_calendar){
                    super.onBackPressed();
                }
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
