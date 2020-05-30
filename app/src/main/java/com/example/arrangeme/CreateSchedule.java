package com.example.arrangeme;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.ScheduleItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSchedule {
    private DatabaseReference mDatabase;
    private  FirebaseFunctions mFunctions;
    ArrayList<AnchorEntity> anchorsList;
    public CreateSchedule(){
        mFunctions = FirebaseFunctions.getInstance();
    }

    public  Task<HttpsCallableResult> classifyUserGroup(String id) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("classifyUser")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        List<String> data = (List<String>) httpsCallableResult.getData();
                        Log.d("classifyUser", "onSuccess: "  );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("classifyUser", "onFailure: " + e );

                    }
                });
    }

    public  Task<HttpsCallableResult> findBestSchedule(int group, ArrayList timeVector, ArrayList frequencyVector, String date) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("group", group);
        data.put("freqVec", frequencyVector);
        data.put("timeVec", timeVector);
        return mFunctions
                .getHttpsCallable("findSchedule")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        List<HashMap<String,String>> data1 = (List<HashMap<String, String>>) httpsCallableResult.getData();
                        Log.d("FINDSCHE", "onSuccess: " + data1);
                        makeFixes(data1,frequencyVector,timeVector,date);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FINDSCHE", "onFailure: " + e );
                    }
                });
    }

    public void makeFixes(List<HashMap<String, String>> recommendedSch, ArrayList requestedFreqVec, ArrayList requestedTimeVector, String date) {
        Log.d("FINDSCHE", "makeFixes: " + recommendedSch.getClass());
        Log.d("FINDSCHE", "makeFixes: " + recommendedSch);

        ArrayList<AnchorEntity> anchorsList= new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(Globals.UID).child("Anchors");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.child("date").getValue().equals(date)){
                        String start = (String) ds.child("startTime").getValue();
                        String end = (String) ds.child("endTime").getValue();
                        String anchorID = (String) ds.getKey();
                        AnchorEntity anch = new AnchorEntity(start,end,anchorID);
                        anchorsList.add(anch);
                    }
                }
                anchorsFix(recommendedSch, requestedFreqVec, requestedTimeVector, anchorsList); //FIRST STEP = CHECK THE ANCHORS
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void anchorsFix(List<HashMap<String, String>> recommendedSch, ArrayList requestedFreqVec, ArrayList requestedTimeVector, ArrayList<AnchorEntity> anchorsList) {
        ArrayList<ScheduleItem> recommendedSchedule = convertStringsToSchedule(recommendedSch);
        
        // works until here, we have the scheudle that the algorithm found in a good arraylist
        //next step, anchor fix (step 1)
    }

    private ArrayList<ScheduleItem> convertStringsToSchedule(List<HashMap<String, String>> recommendedSch) {
        ArrayList<ScheduleItem> items= new ArrayList<>();
        for (Map<String, String> entry : recommendedSch) {
            String startTime = null;
            String endTime = null;
            String category = null;
            for (String key : entry.keySet()) {
                if(key.equals("startTime")) startTime=entry.get(key) ; // get the value (key=startTime, value=09:00)
                if(key.equals("endTime")) endTime=entry.get(key) ; // get the value (key=endTime, value=10:00)
                if(key.equals("category")) category=entry.get(key) ; // get the value (key=category, value=XXXX)
            }
            Log.d("FINDSCHE", "convertStringsToSchedule: item =" + startTime + " " + endTime + " " + category);
            ScheduleItem scheduleItem = new ScheduleItem(startTime,endTime,category);
            items.add(scheduleItem);

        }
        return items;
    }
}
