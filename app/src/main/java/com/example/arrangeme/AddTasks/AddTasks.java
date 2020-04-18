package com.example.arrangeme.AddTasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.example.arrangeme.ChooseTasks;
import com.example.arrangeme.R;
import com.google.android.material.circularreveal.CircularRevealWidget;

import java.util.ArrayList;

public class AddTasks extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar_addTasks);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        Integer[] catIcon = {R.drawable.study, R.drawable.familycat, R.drawable.work, R.drawable.sport,
                R.drawable.nutrition, R.drawable.chores, R.drawable.relax, 0};
        String[] catName = {"Study", "Family", "Work", "Sport", "Nutrition", "Chores", "Relax", "Other"};
        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_family,
                R.drawable.category_btn_work, R.drawable.category_btn_sport,
                R.drawable.category_btn_nutrition, R.drawable.category_btn_chores,
                R.drawable.category_btn_relax, R.drawable.category_btn_other};

        mainModels = new ArrayList<>();

        for (int i = 0; i < catIcon.length; i++) {
            MainModel model = new MainModel(catIcon[i], catName[i], catBackground[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddTasks.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(AddTasks.this, mainModels);
        recyclerView.setAdapter(mainAdapter);



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
           // Toast.makeText(AddTasks.this, "Settings clicked", Toast.LENGTH_LONG).show();
           // return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }
}
