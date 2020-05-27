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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSchedule {
    private DatabaseReference mDatabase;
    private  FirebaseFunctions mFunctions;
    ArrayList<AnchorEntity> anchorsList = new ArrayList<>();
    ArrayList<ScheduleItem> recommendedSchedule;
    ArrayList<ScheduleItem> recommSchWithoutBadHours = new ArrayList<>();
    ArrayList<ScheduleItem> recommSchOnlyWithBadHours = new ArrayList<>();
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
                recommendedSchedule = convertStringsToSchedule(recommendedSch);
                deleteAnchorsFromRecommSch(); //WORKS
                createTempSch1(); // works
                //checkCategoriesFrequency(recommendedSchedule,requestedFreqVec);
                //anchorsFix(recommendedSchedule, requestedFreqVec, requestedTimeVector, anchorsList); //SECOND STEP = CHECK THE ANCHORS
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createTempSch1() {
        for (AnchorEntity anchor : anchorsList){ // comparing each anchor with every item in the suggested schedule
            LocalTime anchorStart = LocalTime.parse( anchor.getStartTime());
            LocalTime anchorEnd = LocalTime.parse( anchor.getEndTime());
            for (ScheduleItem scheduleItem : recommendedSchedule){
                LocalTime scheduleItemStart = LocalTime.parse(scheduleItem.getStartTime());
                LocalTime scheduleItemEnd = LocalTime.parse(scheduleItem.getEndTime());
                if (anchorStart.isBefore(scheduleItemEnd) && scheduleItemStart.isBefore(anchorEnd)) //overlap
                {
                    recommSchOnlyWithBadHours.add(scheduleItem);
                }
                else
                { // no overlap
                    recommSchWithoutBadHours.add(scheduleItem);
                }
            }
        }
    }

    private void deleteAnchorsFromRecommSch() {
        for (int i = 0 ; i < recommendedSchedule.size() ; i++){
            if (recommendedSchedule.get(i).getType().equals("anchor")){
                recommendedSchedule.remove(i);
            }
        }
    }

    private void checkCategoriesFrequency(ArrayList<ScheduleItem> recommendedSchedule, ArrayList requestedFreqVec) {
        for (ScheduleItem scheduleItem : recommendedSchedule) {

        }
        }

    public void anchorsFix(ArrayList<ScheduleItem> recommendedSchedule, ArrayList requestedFreqVec, ArrayList requestedTimeVector, ArrayList<AnchorEntity> anchorsList) {
        // works until here, we have the scheudle that the algorithm found in a good arraylist
        //next step, anchor fix (step 2)
        ArrayList<ScheduleItem> sideItems = new ArrayList<>();
        for (AnchorEntity anchor : anchorsList){ // comparing each anchor with every item in the suggested schedule
            LocalTime anchorStart = LocalTime.parse( anchor.getStartTime());
            LocalTime anchorEnd = LocalTime.parse( anchor.getEndTime());
            for (ScheduleItem scheduleItem : recommendedSchedule){
                LocalTime scheduleItemStart = LocalTime.parse(scheduleItem.getStartTime());
                LocalTime scheduleItemEnd = LocalTime.parse(scheduleItem.getEndTime());
                if (anchorStart.isBefore(scheduleItemEnd) && scheduleItemStart.isBefore(anchorEnd)) // returns true if overlap
                {

                }
            }
        }

    }

    private ArrayList<ScheduleItem> convertStringsToSchedule(List<HashMap<String, String>> recommendedSch) {
        ArrayList<ScheduleItem> items= new ArrayList<>();
        for (Map<String, String> entry : recommendedSch) {
            String startTime = null;
            String endTime = null;
            String category = null;
            String type = null;
            for (String key : entry.keySet()) {
                if(key.equals("startTime")) startTime=entry.get(key) ; // get the value (key=startTime, value=09:00)
                if(key.equals("endTime")) endTime=entry.get(key) ; // get the value (key=endTime, value=10:00)
                if(key.equals("category")) category=entry.get(key) ; // get the value (key=category, value=XXXX)
                if(key.equals("type")) type=entry.get(key);
            }
            Log.d("LINOY", "convertStringsToSchedule: item =" + startTime + " " + endTime + " " + category + type);
            ScheduleItem scheduleItem = new ScheduleItem(startTime,endTime,category,type);
            items.add(scheduleItem);

        }
        return items;
    }
}
