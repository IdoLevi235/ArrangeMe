package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Adminzone extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private Button deleteDB;
    private Button sim1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminzone);
        deleteDB = (Button)findViewById(R.id.dDBbtn);
        sim1 = (Button)findViewById(R.id.sim1btn);

        deleteDB.setOnClickListener(this);
        sim1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.dDBbtn):
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").removeValue();
                break;
            case (R.id.sim1btn):
                simulate1000withPVnoSC();
        }
    }

    private void simulate1000withPVnoSC() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").setValue("simulated_users");

        for(int i = 1 ; i<=1000 ; i++){
            //mDatabase.child("email").setValue()
        }
    }
}
