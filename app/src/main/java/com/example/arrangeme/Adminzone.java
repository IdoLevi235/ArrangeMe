package com.example.arrangeme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Adminzone extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private Button deleteDB;
    private Button sim1;
    private Button deleteSim1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminzone);
        deleteDB = (Button)findViewById(R.id.dDBbtn);
        sim1 = (Button)findViewById(R.id.sim1btn);
        deleteSim1=(Button)findViewById(R.id.dSim1);
        deleteDB.setOnClickListener(this);
        sim1.setOnClickListener(this);
        deleteSim1.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.dDBbtn):
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").removeValue();
                mDatabase.child("simulated_users").removeValue();
                break;
            case (R.id.sim1btn):
                simulate1000withPVnoSC();
                break;

            case (R.id.dSim1):
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("simulated_users").removeValue();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void simulate1000withPVnoSC() {
        mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users");
        for(int i = 1 ; i<=1000 ; i++){
            String s = Integer.toString(i);
            HashMap<String,Integer> pv = calculate_PV();
            mDatabase.child("Sim" + s).child("personality_vector").setValue(pv);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private HashMap<String,Integer> calculate_PV() {
        HashMap<String,Integer> pv = new HashMap<>();
        ArrayList<Integer> randomValues = getRandomValues();
        for (int i=0 ; i<25 ; i++) {
            pv.put(Integer.toString(i+1), randomValues.get(i));
        }
        return pv;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<Integer> getRandomValues() {
        int randomNum1 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        int randomNum2 = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int randomNum3 = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int randomNum4 = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int randomNum5 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum6 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum7 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum8 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum9 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum10 = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int randomNum11 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum12 = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int randomNum13 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        int randomNum14 = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int randomNum15 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum16 = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int randomNum17 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum18 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        int randomNum19 = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        int randomNum20 = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        int randomNum21 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        int randomNum22 = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int randomNum23 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        int randomNum24 = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        int randomNum25 = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        List<Integer> randList = Arrays.asList( randomNum1, randomNum2, randomNum3, randomNum4,randomNum5,
                randomNum6,randomNum7,randomNum8,randomNum9,randomNum10,randomNum11,randomNum12,randomNum13,
                randomNum14,randomNum15,randomNum16,randomNum17,randomNum18,randomNum19,randomNum20,randomNum21,
                randomNum22,randomNum23,randomNum24,randomNum25);
        ArrayList<Integer> randArrayList = new ArrayList<>();
        randArrayList.addAll(randList);
        return randArrayList;

    }
}
