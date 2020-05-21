package com.example.arrangeme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.arrangeme.Enums.ReminderType;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Adminzone extends AppCompatActivity implements View.OnClickListener{
    private final int MORNING = 5; //morning = 06:00-11:00
    private final int NOON = 6; //noon = 11:00-17:00
    private final int EVENING = 4;//evening = 17:00-21:00
    private final int NIGHT = 3; // night = 21:00-24:00
    private static final int NUM_OF_ITERATIONS = 10;
    private static final int NUM_OF_USERS_IN_ITERATION = 5; //dont change it
    private static final int NUM_OF_SCH_PER_USER = 20;
    private DatabaseReference mDatabase;
    private Button deleteDB;
    private Button sim1;
    private Button deleteSim1;
    private Button kmeansBtn;
    private Button node;
    private FirebaseFunctions mFunctions;
    private final int NUM_OF_WINDOWS = 20;
    ArrayList<ScheduleItem> hoursList = new ArrayList<>();
    Map<String, Integer> freqVec = new LinkedHashMap<>();
    Map<String,Integer> timeVec = new LinkedHashMap<>();
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
                simulate();
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
     * Method simulate- simulated  users in the DataBase and create each user a personality vector and schedules
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void simulate() {
       // for (int i=1;i<=NUM_OF_ITERATIONS;i++) {
            //Log.d("TAG7", "simulate: iteration " + i);
//            for (int j = 1; j <= 1000; j++) { //x users
//               Log.d("TAG7", "simulate: user number " + j);
//                String s = Integer.toString(j);
//               //s += j;
//                mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim" + s);
//                HashMap<String, Integer> pv = calculate_PV();
//                mDatabase.child("personality_vector").setValue(pv);
////                createRandomSchedule(j+"-11-2020");
////                //for (int k = 1; k <= NUM_OF_SCH_PER_USER; k++) { //x schedueles per each
////                //    String date = (k + 10) + "-11-2022";
////                //    createRandomSchedule(date);
//                }
//            }
//        for (int k = 900 ; k <= 1000 ; k++) {
//            Log.d("TAG7", "simulate: user number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("29-11-2022");
//            createRandomSchedule("30-11-2022");
//        }
//
//        for(int k = 100 ; k < 200 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//         }
//        for(int k = 200 ; k < 300 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 300 ; k < 400 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 400 ; k < 500 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 500 ; k < 600 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 600 ; k < 700 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 700 ; k < 800 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 800 ; k < 900 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }
//        for(int k = 900 ; k < 1000 ; k++) {
//            Log.d("TAG7", "simulate: sch number " + k);
//            mDatabase = FirebaseDatabase.getInstance().getReference("simulated_users").child("Sim"+k);
//            createRandomSchedule("13-11-2022");
//        }

        //  }
        Log.d("TAG7", "simulate: BYE BYE");

    }

    private void createRandomSchedule(String date) {
        hoursList.clear();
        hoursList.add(new ScheduleItem("06:00", false));
        hoursList.add(new ScheduleItem("06:30", false));
        hoursList.add(new ScheduleItem("07:00", false));
        hoursList.add(new ScheduleItem("07:30", false));
        hoursList.add(new ScheduleItem("08:00", false));
        hoursList.add(new ScheduleItem("08:30", false));
        hoursList.add(new ScheduleItem("09:00", false));
        hoursList.add(new ScheduleItem("09:30", false));
        hoursList.add(new ScheduleItem("10:00", false));
        hoursList.add(new ScheduleItem("10:30", false));
        hoursList.add(new ScheduleItem("11:00", false));
        hoursList.add(new ScheduleItem("11:30", false));
        hoursList.add(new ScheduleItem("12:00", false));
        hoursList.add(new ScheduleItem("12:30", false));
        hoursList.add(new ScheduleItem("13:00", false));
        hoursList.add(new ScheduleItem("13:30", false));
        hoursList.add(new ScheduleItem("14:00", false));
        hoursList.add(new ScheduleItem("14:30", false));
        hoursList.add(new ScheduleItem("15:00", false));
        hoursList.add(new ScheduleItem("15:30", false));
        hoursList.add(new ScheduleItem("16:00", false));
        hoursList.add(new ScheduleItem("16:30", false));
        hoursList.add(new ScheduleItem("17:00", false));
        hoursList.add(new ScheduleItem("17:30", false));
        hoursList.add(new ScheduleItem("18:00", false));
        hoursList.add(new ScheduleItem("18:30", false));
        hoursList.add(new ScheduleItem("19:00", false));
        hoursList.add(new ScheduleItem("19:30", false));
        hoursList.add(new ScheduleItem("20:00", false));
        hoursList.add(new ScheduleItem("20:30", false));
        hoursList.add(new ScheduleItem("21:00", false));
        hoursList.add(new ScheduleItem("21:30", false));
        hoursList.add(new ScheduleItem("22:00", false));
        hoursList.add(new ScheduleItem("22:30", false));
        hoursList.add(new ScheduleItem("23:00", false));
        hoursList.add(new ScheduleItem("23:30", false));
        hoursList.add(new ScheduleItem("00:00", false));
        hoursList.add(new ScheduleItem("00:30", false));


        //(int) ((Math.random() * (max - min)) + min);
        //Set<ScheduleItem> hoursSet = new HashSet<ScheduleItem>();
        ArrayList<Integer> indexes = new ArrayList<>();
        //Random generator = new Random();
        int max = 36;
        int min = 0 ;
        while (indexes.size() < NUM_OF_WINDOWS) {
            float rand = ThreadLocalRandom.current().nextFloat();
            if (rand < 0.20) { //HALF HOURS
                //get random odd number between 0 to 36
                if (min%2==0) ++min;
                int randOdd = min + 2*ThreadLocalRandom.current().nextInt((max-min)/2+1);
                if (Collections.frequency(indexes,randOdd)<2){
                    indexes.add(randOdd);
                }
            }
            else if (rand>=0.20) { // FULL HOURS
                //get random even number between 0 to 36
                int randEven = min + 2*ThreadLocalRandom.current().nextInt((max-min)/2+1);
                if(Collections.frequency(indexes,randEven)<2){
                    indexes.add(randEven);
                }
            }
        }
        Collections.sort(indexes);
        ArrayList<String> categoriesChosen = new ArrayList<>();
        for (int key = 0; key < NUM_OF_WINDOWS; key+=2) {
            // start + end times
            int startIndex=indexes.get(key);
            int endIndex=indexes.get(key+1);
            if (startIndex==endIndex) endIndex++;
            String startHour = hoursList.get(startIndex).getHour();
            //hoursList.get(startIndex).setTaken(true);
            String endHour = hoursList.get(endIndex).getHour();
            //hoursList.get(endIndex).setTaken(true);
            mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("startTime")
                    .setValue(startHour);
            mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("endTime")
                    .setValue(endHour);

            //category and anchor/task stuff
            TaskCategory c = TaskCategory.fromInt((int) ((Math.random() * (8 - 0)) + 0));
            mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("category")
                    .setValue(c); // set random category
           // Random g = new Random();
            float rand = ThreadLocalRandom.current().nextFloat();
            if (rand<0.25) //anchor - not added to either one of the vectors
                mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("type").setValue("anchor");
            else if (rand>=0.25) { //task
                mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("type").setValue("task");
                categoriesChosen.add(c.toString());
                //mark all tasks in this window with isTaken=true
                for (int i=startIndex ; i<endIndex ; i++){
                    hoursList.get(i).setWithTask(true);
                }
            }

            //DESCRIPTION + REMINDER (NOT IMPORTANT)
            mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("description").setValue("desc");
            mDatabase.child("Schedules").child(date).child("schedule").child(String.valueOf(key/2)).child("reminderType")
                       .setValue(ReminderType.fromInt((int) ((Math.random() * (4 - 0)) + 0)));


        }
        createFreqVec(categoriesChosen);
        mDatabase.child("Schedules").child(date).child("data").child("frequency_vector").setValue(freqVec);
        createTimeVec(hoursList);
        mDatabase.child("Schedules").child(date).child("data").child("time_vector").setValue(timeVec);
        mDatabase.child("Schedules").child(date).child("data").child("successful").setValue("yes");

    }

    private void createTimeVec(ArrayList<ScheduleItem> hoursList) {

        int morning=0;
        int noon=0;
        int evening=0;
        int night=0;
        int index=0;

        for(ScheduleItem item : hoursList){
            if (index<MORNING*2){
                if(hoursList.get(index).getWithTask()==true){
                    morning++;
                }
            }
            else if(MORNING*2<=index && index < (NOON*2+MORNING*2)){
                if(hoursList.get(index).getWithTask()==true){
                    noon++;
                }

            }
            else if ((NOON*2+MORNING*2)<=index && index<(EVENING*2+NOON*2+MORNING*2)){
                if(hoursList.get(index).getWithTask()==true){
                    evening++;
                }

            }
            else if ((EVENING*2+NOON*2+MORNING*2)<=index && index<=(EVENING*2+NOON*2+MORNING*2+NIGHT*2)){
                if(hoursList.get(index).getWithTask()==true){
                    night++;
                }

            }
            index++;
        }
        timeVec.put("Morning",morning/2);
        timeVec.put("Noon",noon/2);
        timeVec.put("Evening",evening/2);
        timeVec.put("Night",night/2);
    }

    private void createFreqVec(ArrayList<String> categoriesChosen) {
        //Map<String,Integer> freqVec = new LinkedHashMap<>();
        freqVec.put("Study", Collections.frequency(categoriesChosen,"STUDY"));
        freqVec.put("Sport", Collections.frequency(categoriesChosen,"SPORT"));
        freqVec.put("Work", Collections.frequency(categoriesChosen,"WORK"));
        freqVec.put("Nutrition", Collections.frequency(categoriesChosen,"NUTRITION"));
        freqVec.put("Family", Collections.frequency(categoriesChosen,"FAMILY"));
        freqVec.put("Chores", Collections.frequency(categoriesChosen,"CHORES"));
        freqVec.put("Relax", Collections.frequency(categoriesChosen,"RELAX"));
        freqVec.put("Friends", Collections.frequency(categoriesChosen,"FRIENDS"));
        freqVec.put("Other", Collections.frequency(categoriesChosen,"OTHER"));
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
