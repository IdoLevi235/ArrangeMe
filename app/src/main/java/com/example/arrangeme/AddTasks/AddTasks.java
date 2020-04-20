package com.example.arrangeme.AddTasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.ChooseTasks;
import com.example.arrangeme.Globals;
import com.example.arrangeme.R;
import com.google.android.material.circularreveal.CircularRevealWidget;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddTasks extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    Toolbar toolbar;
    Button rightScrl;
    Button leftScrl;
    Button confirmBtn;
    int currentPosition;
    final int numOfCategories = 8;
    TextView textViewHelloAdd;
    EditText desc;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar_addTasks);
        setSupportActionBar(toolbar);
        leftScrl=findViewById(R.id.btnLeftScrl);
        rightScrl=findViewById(R.id.btnRightScrl);
        confirmBtn=findViewById(R.id.sumbitBtn11);
        desc=findViewById(R.id.text_desc);
        /* Recycler View Stuff */
        recyclerView = findViewById(R.id.recycler_view);
        Integer[] catIcon = {R.drawable.study, R.drawable.sport,  R.drawable.work, R.drawable.nutrition,
                R.drawable.familycat, R.drawable.chores, R.drawable.relax, 0};
        String[] catName = {"Study", "Sport", "Work", "Nutrition", "Family", "Chores", "Relax", "Other"};
        Integer[] catBackground = {R.drawable.category_btn_study, R.drawable.category_btn_sport,
                R.drawable.category_btn_work, R.drawable.category_btn_nutrition,
                R.drawable.category_btn_family, R.drawable.category_btn_chores,
                R.drawable.category_btn_relax, R.drawable.category_btn_other};
        Integer[] catBackgroundFull =
                {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_other_nostroke};

        Integer[] catColor={R.color.study, R.color.sport, R.color.work, R.color.nutrition,
                R.color.family, R.color.chores, R.color.relax, R.color.other};
        mainModels = new ArrayList<>();

        for (int i = 0; i < catIcon.length; i++) {
            MainModel model = new MainModel(catIcon[i], catName[i], catBackground[i],catColor[i],catBackgroundFull[i]);
            mainModels.add(model);
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(AddTasks.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(AddTasks.this, mainModels);
        recyclerView.setAdapter(mainAdapter);
        leftScrl.setVisibility(View.INVISIBLE);
        rightScrl.setVisibility(View.VISIBLE);
        rightScrl.setBackgroundResource(0);
        leftScrl.setBackgroundResource(0);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    currentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                   //onPageChanged(position);
                    SwitchArrows();
                }
                else if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                    SwitchArrows();
                }
                System.err.println(currentPosition);
            }
            });


        /* Recycler View Stuff End*/

        /* Right and Left click listenrs */
        rightScrl.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerView.smoothScrollBy(300, 0);
                rightScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                rightScrl.setBackgroundResource(0);
            }
            return true;
        });

        leftScrl.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerView.smoothScrollBy(-300, 0);
                leftScrl.setBackgroundResource(R.drawable.rounded_rec_gray);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                leftScrl.setBackgroundResource(0);
            }
            return true;
        });
        /* Right and Left click listenrs end*/
        textViewHelloAdd = findViewById(R.id.textViewHelloAdd);
        textViewHelloAdd.setText("Hello, " + Globals.currentUsername + "!");

        /* confirm button click listener */
        confirmBtn.setOnClickListener(v -> {
            if(!MainAdapter.isClicked) {
                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must choose a category!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }

            if (desc.getText().toString().trim().length() == 0){
                SweetAlertDialog ad = new SweetAlertDialog(AddTasks.this, SweetAlertDialog.ERROR_TYPE);
                ad.setTitleText("Error");
                ad.setContentText("You must enter task description!");
                ad.show();
                Button btn = (Button) ad.findViewById(R.id.confirm_button);
                btn.setBackgroundResource(R.drawable.rounded_rec);
            }
        });
        /* confirm button click listener */


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





    public void SwitchArrows(){
        leftScrl=findViewById(R.id.btnLeftScrl);
        rightScrl=findViewById(R.id.btnRightScrl);
        if (currentPosition == 0) {
            leftScrl.setVisibility(View.INVISIBLE);
            rightScrl.setVisibility(View.VISIBLE);
        } else if (currentPosition == numOfCategories - 3) {
            leftScrl.setVisibility(View.VISIBLE);
            rightScrl.setVisibility(View.INVISIBLE);
        } else {
            leftScrl.setVisibility(View.VISIBLE);
            rightScrl.setVisibility(View.VISIBLE);
        }
    }
}

//TODO: toolbar items
//todo: better EditTexts