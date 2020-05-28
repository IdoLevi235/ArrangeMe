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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreateSchedule {
    private DatabaseReference mDatabase;
    private  FirebaseFunctions mFunctions;
    ArrayList<AnchorEntity> anchorsList = new ArrayList<>();
    ArrayList<ScheduleItem> recommendedSchedule;
    ArrayList<ScheduleItem> recommSchGoodHours = new ArrayList<>();
    ArrayList<ScheduleItem> recommSchBadHours = new ArrayList<>();
    HashMap<String,Integer> requestedFreqVec = new HashMap<>();
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
        initFreqVecHashMap(frequencyVector); // storing the requested freq vec for later use
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

    private void initFreqVecHashMap(ArrayList frequencyVector) {
        requestedFreqVec.put("STUDY", (Integer) frequencyVector.get(0));
        requestedFreqVec.put("SPORT", (Integer) frequencyVector.get(1));
        requestedFreqVec.put("WORK", (Integer) frequencyVector.get(2));
        requestedFreqVec.put("NUTRITION", (Integer) frequencyVector.get(3));
        requestedFreqVec.put("FAMILY", (Integer) frequencyVector.get(4));
        requestedFreqVec.put("CHORES", (Integer) frequencyVector.get(5));
        requestedFreqVec.put("RELAX", (Integer) frequencyVector.get(6));
        requestedFreqVec.put("FRIENDS", (Integer) frequencyVector.get(7));
        requestedFreqVec.put("OTHER", (Integer) frequencyVector.get(8));

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
                deleteAnchorsFromRecommSch(); // WORKS
                divideReccSch(); // works
                badHoursCheck(); // works
                goodHoursCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goodHoursCheck() {
        String newCat = "";
        String goodItemCategory="";
        for (int i = 0 ; i<recommSchGoodHours.size();i++){ // big loop with index  i - on goodHours items
            goodItemCategory = recommSchGoodHours.get(i).getCategory();
            Integer x = requestedFreqVec.get(goodItemCategory); // get the value of this category in freq vec
            if (x>0){
                requestedFreqVec.replace(goodItemCategory, x, x - 1);
            }
            else { // replace it with something from the bad hours, or delete it from good hours
                if (!recommSchBadHours.isEmpty()){ // bad hours is not empty
                    boolean flag=false;
                    for (int j = 0 ; j<recommSchBadHours.size() ; j++){ // finding item in bad hours that matches the freq vec
                        if (flag==true) break; // stop looping
                        newCat = recommSchBadHours.get(j).getCategory();
                        x = requestedFreqVec.get(newCat);
                        if (x>0) { // if we found item in bad hours that matches the freqvec
                            recommSchGoodHours.get(i).setCategory(newCat); // replace category in the good list
                            recommSchBadHours.remove(j); // remove this item from bad list
                            requestedFreqVec.replace(newCat, x, x - 1); // update freq vec
                            flag=true; // stop looping
                        }
                    }

                }
                else { // bad hours empty, so just delete it from good list
                    recommSchGoodHours.remove(i);
                }
            }
        }

    }

    private void badHoursCheck() {
        Iterator<ScheduleItem> it = recommSchBadHours.iterator();
        while(it.hasNext()) {
            String badItemCategory = it.next().getCategory();
            Integer x = requestedFreqVec.get(badItemCategory); // get the value of this category in freq vec
            if (x > 0) {   // it is in freqvec
                requestedFreqVec.replace(badItemCategory, x, x - 1);
            } else {
                it.remove();
            }
        }
    }

    private void divideReccSch() {
        for (AnchorEntity anchor : anchorsList){ // comparing each anchor with every item in the suggested schedule
            LocalTime anchorStart = LocalTime.parse( anchor.getStartTime());
            LocalTime anchorEnd = LocalTime.parse( anchor.getEndTime());
            for (ScheduleItem scheduleItem : recommendedSchedule){
                LocalTime scheduleItemStart = LocalTime.parse(scheduleItem.getStartTime());
                LocalTime scheduleItemEnd = LocalTime.parse(scheduleItem.getEndTime());
                if (anchorStart.isBefore(scheduleItemEnd) && scheduleItemStart.isBefore(anchorEnd)) //overlap
                {
                    recommSchBadHours.add(scheduleItem);
                }
                else
                { // no overlap
                    recommSchGoodHours.add(scheduleItem);
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
