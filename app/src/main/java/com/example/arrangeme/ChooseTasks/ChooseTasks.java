package com.example.arrangeme.ChooseTasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.Enums.TaskCategory;
import com.example.arrangeme.Globals;
import com.example.arrangeme.Homepage;
import com.example.arrangeme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChooseTasks extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private int numOfTasksToChoose = 4; //needs to be recieved from DB
    private int count;
    private TextView numberTextView;
    private TextView howMuchMore;
    private TextView helloTxt;
    private Button confirm;
    private RecyclerView mRecycler;
    private RelativeLayout rl;
    private View view;
    private LinearLayoutManager layoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerOptions<MainModel> options;
    private FirebaseRecyclerAdapter<MainModel, MyViewHolder> fbAdapter;
    private TextView tv;
    private TextView tv2;
    private ProgressBar spinner;
    private Button setDate;
    Integer[] catIcon = {R.drawable.study_white, R.drawable.sport_white,
            R.drawable.work_white, R.drawable.nutrition_white,
            R.drawable.family_white, R.drawable.chores_white,
            R.drawable.relax_white, R.drawable.friends_white, 0};
    Integer[] catBackgroundFull =
            {R.drawable.rounded_rec_study_nostroke, R.drawable.rounded_rec_sport_nostroke,
                    R.drawable.rounded_rec_work_nostroke, R.drawable.rounded_rec_nutrition_nostroke,
                    R.drawable.rounded_rec_family_nostroke, R.drawable.rounded_rec_chores_nostroke,
                    R.drawable.rounded_rec_relax_nostroke, R.drawable.rounded_rec_friends_nostroke,
                    R.drawable.rounded_rec_other_nostroke};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar_chooseTasks);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.progressBar3);
        spinner.setVisibility(View.VISIBLE);

        tv = findViewById(R.id.textView16);
        tv.setVisibility(View.GONE);

        tv2=findViewById(R.id.textViewExplanation);
        tv2.setVisibility(View.INVISIBLE);

        numberTextView = (TextView) findViewById(R.id.textViewNumbersRed);
        numberTextView.setText(Integer.toString(count));
        numberTextView.setBackgroundResource(R.drawable.red_textview);

        howMuchMore = (TextView) findViewById(R.id.textViewHowManyMore);
        howMuchMore.setText("You have to choose " + (numOfTasksToChoose - count) + " more tasks");

        helloTxt = (TextView) findViewById(R.id.textViewHello);
        helloTxt.setText("Hello, " + Globals.currentUsername + "!");

        count = 0;

        confirm = (Button) findViewById(R.id.confirmTasksBtn);
        confirm.setOnClickListener(this);

        setDate = (Button)findViewById(R.id.chooseDate);
        setDate.setOnClickListener(this);
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        Log.d("TAG3", "onCreate: " + date);
        if (date!=null) {
            setDate.setText(date);
        }

        layoutManager = new LinearLayoutManager(ChooseTasks.this, LinearLayoutManager.VERTICAL, false);
        mRecycler = findViewById(R.id.recylcler_choosetasks);
        setRecycler(mRecycler);

    }

    private void setRecycler(RecyclerView mRecyclerView) {
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Pending_tasks");
        options = new FirebaseRecyclerOptions.Builder<MainModel>().setQuery(mDatabase, MainModel.class).build();
        checkIfThereArePendingTasks(mDatabase);
        /* Fire base UI stuff */
        fbAdapter = new FirebaseRecyclerAdapter<MainModel, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModel model) {
                holder.button.setText("\t" + model.getCategory() + " \n\n\t" + model.getDescription());
                holder.button.setLayoutParams(new LinearLayout.LayoutParams(800, 180));
                int x = TaskCategory.fromStringToInt(model.getCategory());
                holder.button.setBackgroundResource(catBackgroundFull[x]);
                holder.button.setCompoundDrawablesWithIntrinsicBounds(0, 0, catIcon[x], 0);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemOnClick(holder, position, model);
                    }
                });
                spinner.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tasks_tab, parent, false);
                return new MyViewHolder(v);
            }

        };
        fbAdapter.startListening();
        mRecycler.setAdapter(fbAdapter);
        /* Fire base UI stuff End */

    }

    private void itemOnClick(MyViewHolder holder, int position, MainModel model) {
        Drawable dDarkblue = ResourcesCompat.getDrawable(getResources(),
                R.drawable.rounded_rec_darkblue_nostroke, null);
        Drawable dCurr = holder.button.getBackground();
        Drawable.ConstantState constantStateDrawableA = dDarkblue.getConstantState();
        Drawable.ConstantState constantStateDrawableB = dCurr.getConstantState();
        if (!constantStateDrawableA.equals(constantStateDrawableB)) { //Not pressed yet
            chooseTask(holder);
        } else { //pressed, unpick
            unChooseTask(holder, model);
        }
    }

    private void unChooseTask(MyViewHolder holder, MainModel model) {
        int x = TaskCategory.fromStringToInt(model.getCategory());
        holder.button.setBackgroundResource(catBackgroundFull[x]);
        count--;
        if (count == numOfTasksToChoose - 1) { //green-->red
            numberTextView.setBackgroundResource(R.drawable.red_textview);
        }
        numberTextView.setText(Integer.toString(count));
        howMuchMore.setText("(You have to choose " + Integer.toString(numOfTasksToChoose - count) + " more tasks..)");
    }

    private void chooseTask(MyViewHolder holder) {
        if (count < numOfTasksToChoose) { //stay red
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
            numberTextView.setBackgroundResource(R.drawable.red_textview);
            numberTextView.setText(Integer.toString(count));
            howMuchMore.setText("(You have to choose " + (numOfTasksToChoose - count) + " more tasks..)");
        } else if (count == numOfTasksToChoose - 1) { //red--->green
            count++;
            holder.button.setBackgroundResource(R.drawable.rounded_rec_darkblue_nostroke);
            numberTextView.setBackgroundResource(R.drawable.green_textview);
            numberTextView.setText(Integer.toString(count));
            howMuchMore.setText("(You have to choose " + (numOfTasksToChoose - count) + " more tasks..)");
        } else if (numOfTasksToChoose == count) { //more than numOfTasks, uncheck the last one checked
            Toast.makeText(getApplicationContext(), "You can choose only " + numOfTasksToChoose + " !", Toast.LENGTH_SHORT).show();
        }

    }


    private void checkIfThereArePendingTasks(DatabaseReference mDatabase) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    tv.setText("You have no pending tasks");
                    tv.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        // if (id == R.id.action_settings) {
        //    Toast.makeText(ChooseTasks.this, "Settings clicked", Toast.LENGTH_LONG).show();
        //return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTasksBtn:
              if (setDate.getText().length()==0){
                  String str = "You must choose a date!";
                  chooseTaskFailed(str);

              }
                else if (count < numOfTasksToChoose) {
                String str = "You must choose " + numOfTasksToChoose + " tasks!";
                chooseTaskFailed(str);
            }
                else
                    chooseTaskSuccess();
                break;

            case R.id.chooseDate:
                DatePickerDialog datePickerDialog = createDatePickerDialog();
                datePickerDialog.show();

            default:
                break;
        }//end of switch

    } //end of onclick

    private void chooseTaskFailedMustPickDate() {
    }

    private DatePickerDialog createDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                setDate.setText(date);
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("According to your anchors, in " +date+" you have 4 free-time windows. You have to choose 4 tasks.");
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        return dpd;
    }

    private void chooseTaskSuccess() {
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog( ChooseTasks.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Great!")
                .setContentText(("The system received your tasks for today and will build you schedule immediately"));
        ad.setConfirmText("OK");
        ad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(ChooseTasks.this, Homepage.class));
            }
        });
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);
    }


    private void chooseTaskFailed(String str) {
        SweetAlertDialog ad;
        ad =  new SweetAlertDialog(ChooseTasks.this, SweetAlertDialog.WARNING_TYPE);
        ad.setTitleText("Error");
        ad.setContentText(str);
        ad.show();
        Button btn = (Button) ad.findViewById(R.id.confirm_button);
        btn.setBackgroundResource(R.drawable.rounded_rec);
    }
}

//TODO: toolbar items
