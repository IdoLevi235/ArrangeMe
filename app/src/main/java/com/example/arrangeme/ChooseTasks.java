package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrangeme.Questionnaire.Questionnaire;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChooseTasks extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private int numOfTasksToChoose = 4; //needs to be recieved from DB
    private int count;
    private TextView redTextView;
    private TextView greenTextView;
    private TextView howMuchMore;
    private TextView helloTxt;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar_chooseTasks);
        setSupportActionBar(toolbar);
        ScrollView sv = (ScrollView) findViewById(R.id.tasksScrollView);
        LinearLayout ll =(LinearLayout)findViewById(R.id.tasksLinearLayout);
        redTextView = (TextView)findViewById(R.id.textViewNumbersRed);
        greenTextView = (TextView)findViewById(R.id.textViewNumbersGreen);
        howMuchMore = (TextView)findViewById(R.id.textViewHowManyMore);
        confirm=(Button)findViewById(R.id.confirmTasksBtn);
        helloTxt=(TextView)findViewById(R.id.textViewHello);
        ll.setOrientation(LinearLayout.VERTICAL);
        for(int i = 0; i < 20; i++) {
            CheckBox ch = new CheckBox(ChooseTasks.this);
            ch.setTextAppearance(this, R.style.chooseTasksCheckBoxStyle);
            ch.setText("Checkbox number " + (i+1));
            ch.setOnClickListener(this);
            ll.addView(ch);
        }
        count=0;
        helloTxt.setText("Hello, " + Globals.currentUsername + "!");
        redTextView.setText(Integer.toString(count));
        redTextView.setBackgroundResource(R.drawable.red_button_background);
        greenTextView.setVisibility(View.GONE);
        howMuchMore.setText("(You have to choose " + (numOfTasksToChoose-count) + " more tasks..)");
        confirm.setOnClickListener(this);

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
        if (id == R.id.action_settings) {
            Toast.makeText(ChooseTasks.this, "Settings clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        TextView redTextView = (TextView)findViewById(R.id.textViewNumbersRed);
        //TextView greenTextView = (TextView)findViewById(R.id.textViewNumbersGreen);
        TextView howMuch = (TextView) findViewById (R.id.textViewHowManyMore);
        switch (v.getId()){
            case R.id.confirmTasksBtn:
                if (count<numOfTasksToChoose)
                {
                    SweetAlertDialog ad;
                    ad =  new SweetAlertDialog(ChooseTasks.this, SweetAlertDialog.WARNING_TYPE);
                    ad.setTitleText("Error");
                    ad.setContentText("You must choose " + numOfTasksToChoose + " tasks!");
                    ad.show();
                    Button btn = (Button) ad.findViewById(R.id.confirm_button);
                    btn.setBackgroundResource(R.drawable.rounded_rec);
                }
                else {
                    SweetAlertDialog ad;
                    ad =  new SweetAlertDialog( ChooseTasks.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Great!")
                            .setContentText(("The system recieved your tasks for today and will build you schedule immediatley"));
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
                break;

            default: //checkboxes
                CheckBox c = ((CheckBox) v);
                if(c.isChecked()) { //check
                    if (count==numOfTasksToChoose-1){ //red--->green
                        count++;
                        redTextView.setBackgroundResource(R.drawable.green_button_background);
                        redTextView.setText(Integer.toString(count));

                        // greenTextView.setVisibility(View.VISIBLE)
                        // greenTextView.setText(Integer.toString(count));
                        howMuchMore.setText("(You have to choose " + (numOfTasksToChoose-count) + " more tasks..)");
                    }
                    else if (count < numOfTasksToChoose) { //stay red
                        count++;
                        redTextView.setBackgroundResource(R.drawable.red_button_background);
                       // greenTextView.setVisibility(View.GONE);
                        redTextView.setText(Integer.toString(count));
                        howMuchMore.setText("(You have to choose " + (numOfTasksToChoose-count) + " more tasks..)");
                    }
                    else if (numOfTasksToChoose == count) { //more than numOfTasks, uncheck the last one checked
                        Toast.makeText(getApplicationContext(), "You can choose only " + numOfTasksToChoose + " !", Toast.LENGTH_SHORT).show();
                        c.setChecked(false);
                    }
                }//end if

                else { //uncheck
                    count--;
                    if (count==numOfTasksToChoose-1){ //green-->red
                        //greenTextView.setVisibility(View.GONE);
                        redTextView.setBackgroundResource(R.drawable.red_button_background);
                    }
                    redTextView.setText(Integer.toString(count));
                    howMuchMore.setText("(You have to choose " + Integer.toString(numOfTasksToChoose-count) + " more tasks..)");
                }//end else
        break;
        }//end of switch

        } //end of onclick
    }

