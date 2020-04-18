package com.example.arrangeme.AddTasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.arrangeme.R;
import com.google.android.material.circularreveal.CircularRevealWidget;

import java.util.ArrayList;

public class AddTasks extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
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
}
