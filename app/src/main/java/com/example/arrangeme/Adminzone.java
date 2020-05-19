package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.arrangeme.Enums.TaskCategory;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Adminzone extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private Button deleteDB;
    private Button sim1;
    private Button deleteSim1;
    private Button kmeansBtn;
    private Button node;
    private FirebaseFunctions mFunctions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctions = FirebaseFunctions.getInstance();
        setContentView(R.layout.activity_adminzone);
        deleteDB = (Button)findViewById(R.id.dDBbtn);
        sim1 = (Button)findViewById(R.id.sim1btn);
        deleteSim1=(Button)findViewById(R.id.dSim1);
        kmeansBtn =(Button)findViewById(R.id.kmeansBtn);
        kmeansBtn.setOnClickListener(this);
        deleteDB.setEnabled(false);
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
                break;
            case(R.id.kmeansBtn):
                Task<HttpsCallableResult> result = kmeans("text");
                break;
        }
    }

    /**
     * Method kmeans- classify the simulated users to their group of people using kmeans function.
     * this function is activated in "Firebase Functions" and called "initKmeans"
     *
     * @return a result if the algorithm Kmeans succeeded ot not
     */
    public Task<HttpsCallableResult> kmeans(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);
        return mFunctions
                .getHttpsCallable("initKmeans")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        List<String> data = (List<String>) httpsCallableResult.getData();
                        Log.d("kmeans", "onSuccess: "  );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("kmeans", "onFailure: " + e );

                    }
                });
    }

    /**
     * Method simulate1000withPVnoSC- simulated 1000 users in the DataBase and create each user a personality vector.
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void simulate1000withPVnoSC() {
        for(int i = 1 ; i<=2 ; i++){ //1000 users
            String s = Integer.toString(i);
            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim" + s);
            HashMap<String,Integer> pv = calculate_PV();
            mDatabase.child("personality_vector").setValue(pv);
            for (int j = 1 ; j<=10 ; j++){ //10 schedueles per each
                String date = (j+10) + "-11-2022";
                createRandomSchedule(date);
            }
        }

    }

    private void createRandomSchedule(String date) {
        int key = 0;
        ArrayList<String> hours = new ArrayList<String>()
        {{
            add("06:00");add("06:30");add("07:00");add("07:30");add("08:00");add("08:30");
            add("09:00");add("09:30");add("10:00");add("10:30");add("11:00");add("11:30");
            add("12:00");add("12:30");add("13:00");add("13:30");add("14:00");add("14:30");
            add("15:00");add("15:30");add("16:00");add("16:30");add("17:00");add("17:30");
            add("18:00");add("18:30");add("19:00");add("19:30");add("20:00");add("20:30");
            add("21:00");add("21:30");add("22:00");add("22:30");add("23:00");add("23:30");
            add("00:00");
        }};

        for (key=0;hours.size()>2;key++) { //the loop continues as long as there are at least 1 free hours in the "bank"
            int hoursSize = hours.size();
            mDatabase.child("Schedules").child(date).child("tempschedule").child(String.valueOf(key)).child("category")
                    .setValue(TaskCategory.fromInt(ThreadLocalRandom.current().nextInt(0, 8 + 1))); // set random category
            int randStartTimeIndex = (ThreadLocalRandom.current().nextInt(0,(hoursSize-1))); // get random start time
            mDatabase.child("Schedules").child(date).child("tempschedule").child(String.valueOf(key)).child("startTime") // write random start time to DB
                    .setValue(hours.get(randStartTimeIndex));
            int randEndTimeIndex = (ThreadLocalRandom.current().nextInt(randStartTimeIndex+1,(hoursSize-1) + 1)); // get random start time
            mDatabase.child("Schedules").child(date).child("tempschedule").child(String.valueOf(key)).child("endTime")
                    .setValue(hours.get(randEndTimeIndex));
                    hours.subList(randStartTimeIndex,randEndTimeIndex+1).clear();

        }
        }


    /**
     * Method calculate_PV- calculating the personality vector to each simulated user.
     *
     * @return HashMap with the key (0,...,25) and the random value for each key
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HashMap<String,Integer> calculate_PV() {
        HashMap<String,Integer> pv = new HashMap<String,Integer>();
        ArrayList<Integer> randomValues = getRandomValues();
        for (int i=0 ; i<25 ; i++) {
            pv.put(Integer.toString(i+1), randomValues.get(i));
        }
        return pv;
    }

    /**
     * Method getRandomValues- calculating the random values for the personality vector.
     *
     * @return ArrayList with the random values
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Integer> getRandomValues() {
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
        ArrayList<Integer> randArrayList = new ArrayList<Integer>();
        randArrayList.addAll(randList);
        return randArrayList;

    }
}
