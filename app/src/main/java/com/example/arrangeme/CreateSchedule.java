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
                        List<String> data = (List<String>) httpsCallableResult.getData();
                        Log.d("FINDSCHE", "onSuccess: " +data.get(0));
                        Log.d("FINDSCHE", "onSuccess: " +data.getClass());
                        Log.d("FINDSCHE", "onSuccess: "  +data);
                        //makeFixes(data,frequencyVector,timeVector,date);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FINDSCHE", "onFailure: " + e );
                    }
                });
    }

    public void makeFixes(List<String> recommendedSch, ArrayList requestedFreqVec, ArrayList requestedTimeVector, String date) {
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

    public void anchorsFix(List<String> recommendedSch, ArrayList requestedFreqVec, ArrayList requestedTimeVector, ArrayList<AnchorEntity> anchorsList) {
        ArrayList<ScheduleItem> reccomendedSchedule = convertStringsToSchedule(recommendedSch);
    }

    private ArrayList<ScheduleItem> convertStringsToSchedule(List<String> recommendedSch) {
        ArrayList<ScheduleItem> items= new ArrayList<>();
        for (String item:recommendedSch){
            String bigArr[] = item.split(", ");
            String startTimeArr[] = bigArr[2].split("=");
            String endTimeArr[] = bigArr[3].split("=");
            String categoryArr[] = bigArr[4].split("=");
            ScheduleItem sc = new ScheduleItem(startTimeArr[1],endTimeArr[1],categoryArr[1]);
            items.add(sc);
        }

        Log.d("convertStringsToSchedule", "convertStringsToSchedule: " +items.get(0).getStartTime()+" "+ items.size());
        return items;
    }
}
