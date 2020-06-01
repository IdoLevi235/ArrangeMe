package com.example.arrangeme;

import android.app.PendingIntent;
import android.transition.Scene;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Entities.ScheduleItem;
import com.example.arrangeme.Entities.TaskEntity;
import com.example.arrangeme.Enums.TaskCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CreateSchedule {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private  FirebaseFunctions mFunctions;
    ArrayList<String> onlyAvailableHoursList = new ArrayList<>();
    ArrayList<AnchorEntity> anchorsList = new ArrayList<>();
    ArrayList<ScheduleItem> recommendedSchedule;
    ArrayList<ScheduleItem> recommSchGoodHours = new ArrayList<>();
    ArrayList<ScheduleItem> recommSchBadHours = new ArrayList<>();
    HashMap<String,Integer> requestedFreqVec = new HashMap<>();
    HashMap<String,Integer> originalRequestedFreqVec = new HashMap<>();
    ArrayList<ScheduleItem> finalSchedule = new ArrayList<>();
    ArrayList<ScheduleItem> tempTasks = new ArrayList<>();
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

    public  Task<HttpsCallableResult> findBestSchedule(int group, ArrayList timeVector, ArrayList frequencyVector, String date, ArrayList<String> keysChosen) {
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
                        makeFixes(data1,frequencyVector,timeVector,date,keysChosen);
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
        originalRequestedFreqVec.put("STUDY", (Integer) frequencyVector.get(0));
        originalRequestedFreqVec.put("SPORT", (Integer) frequencyVector.get(1));
        originalRequestedFreqVec.put("WORK", (Integer) frequencyVector.get(2));
        originalRequestedFreqVec.put("NUTRITION", (Integer) frequencyVector.get(3));
        originalRequestedFreqVec.put("FAMILY", (Integer) frequencyVector.get(4));
        originalRequestedFreqVec.put("CHORES", (Integer) frequencyVector.get(5));
        originalRequestedFreqVec.put("RELAX", (Integer) frequencyVector.get(6));
        originalRequestedFreqVec.put("FRIENDS", (Integer) frequencyVector.get(7));
        originalRequestedFreqVec.put("OTHER", (Integer) frequencyVector.get(8));

    }

    public void makeFixes(List<HashMap<String, String>> recommendedSch, ArrayList requestedFreqVec, ArrayList requestedTimeVector, String date, ArrayList<String> keysChosen) {
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
                        String category = (String) ds.child("category").getValue();
                        AnchorEntity anch = new AnchorEntity(start,end,anchorID,category);
                        anchorsList.add(anch);
                    }
                }
                recommendedSchedule = convertStringsToSchedule(recommendedSch);
                deleteAnchorsFromRecommSch(); // WORKS - deleting anchors from recommended schedule
                divideReccSch(); // works - 2 lists: 1) recommended tasks in bad hours (anchors of the requesting user)  2) recommended tasks in good hours
                badHoursCheck(); // works - step 1 in the fix algorithm
                goodHoursCheck(); //works - step 2 in the fix algorithm
                unifyGoodScheduleWithAnchors(); // good schedule + anchors unified
                finalCheck(); // step 3, here we get final schedule
                getTempsDetails(date);
                //moveTasksFromTempToPending(); // if any tasks left in temp (not chosen to final schedule), put them back in pending
                // Unchosen tasks back to pending tasks


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTempsDetails(String date) { // key = schedule item key
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("tasks").child("temp");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            String category = (String) ds.child("category").getValue();
                            String createDate = (String) ds.child("createDate").getValue();
                            String description = (String) ds.child("description").getValue();
                            String location = (String) ds.child("location").getValue();
                            String photoUri = (String) ds.child("photoUri").getValue();
                            String reminderType = (String) ds.child("reminderType").getValue();
                            ScheduleItem item = new ScheduleItem( category,  createDate,  description,  location,  photoUri,  reminderType);
                            String id = ds.getKey();
                            item.setId(Integer.parseInt(id));
                            tempTasks.add(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                Log.d("koosemek", "onDataChange: " + tempTasks);
                addTasksToDB(date);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addTasksToDB(String date) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference scheduleRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("Schedules").child(date).child("schedule");
        int key = -1 ;
        for (ScheduleItem item : finalSchedule){
            key++;
            scheduleRef.child(String.valueOf(key)).child("startTime").setValue(item.getStartTime());
            scheduleRef.child(String.valueOf(key)).child("endTime").setValue(item.getEndTime());
            if (!item.getType().equals("anchor")) {
                for (ScheduleItem task : tempTasks) {
                    if (item.getCategory().equals(task.getCategory())) { // match
                        scheduleRef.child(String.valueOf(key)).child("type").setValue("task");
                        scheduleRef.child(String.valueOf(key)).child("category").setValue(task.getCategory());
                        scheduleRef.child(String.valueOf(key)).child("createDate").setValue(task.getCreateDate());
                        scheduleRef.child(String.valueOf(key)).child("date").setValue(date);
                        scheduleRef.child(String.valueOf(key)).child("description").setValue(task.getDescription());
                        scheduleRef.child(String.valueOf(key)).child("location").setValue(task.getLocation());
                        scheduleRef.child(String.valueOf(key)).child("photoUri").setValue(task.getPhotoUri());
                        scheduleRef.child(String.valueOf(key)).child("reminderType").setValue(task.getReminderType());
                        //after task added to db, we want to remove it from temp into active tasks
                        DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("tasks").child("Active_tasks");
                        task.setDate(date);
                        activeRef.push().setValue(task);
                        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("tasks").child("temp");
                        tempRef.child(String.valueOf(task.getId())).setValue(null);
                        tempTasks.remove(task);
                        break;
                    }
                }
            }

            else if (item.getType().equals("anchor")){
                scheduleRef.child(String.valueOf(key)).child("AnchorID").setValue(item.getAnchorID());
                scheduleRef.child(String.valueOf(key)).child("type").setValue("anchor");
                scheduleRef.child(String.valueOf(key)).child("category").setValue(item.getCategory());
            }

        } // end of schedule creation

        //now we need to put in Pending tasks the tasks that are still in temp
        moveTasksFromTempToPending();

    }

    private void moveTasksFromTempToPending() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("tasks").child("temp");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String key = ds.getKey();
                    String category = (String) ds.child("category").getValue();
                    String createDate = (String) ds.child("createDate").getValue();
                    String description = (String) ds.child("description").getValue();
                    String location = (String) ds.child("location").getValue();
                    String reminderType = (String) ds.child("reminderType").getValue();
                    String photoURI = (String) ds.child("photoUri").getValue();
                    ds.getRef().setValue(null);
                    putInPendingTasks(key,category,createDate,description,location,reminderType,photoURI);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void putInPendingTasks(String key, String category, String createDate, String description, String location, String reminderType, String photoURI) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("tasks").child("Pending_tasks");
        newRef.child(key).child("category").setValue(category);
        newRef.child(key).child("createDate").setValue(createDate);
        newRef.child(key).child("description").setValue(description);
        newRef.child(key).child("location").setValue(location);
        newRef.child(key).child("reminderType").setValue(reminderType);
        newRef.child(key).child("photoUri").setValue(photoURI);
    }

    private void finalCheck(){
            HashMap<String,Integer> currentFreqVec = new HashMap<String,Integer>() {{
                put("STUDY",0); put("SPORT",0); put("WORK",0); put("NUTRITION",0); put("FAMILY",0);
                put("CHORES",0); put("RELAX",0); put("FRIENDS",0); put("OTHER",0);
            }};
            HashMap<String,Integer> difference = new HashMap<>(); // difference between original and current
        LinkedHashMap<String,Boolean> hoursMap = new LinkedHashMap<String,Boolean>() // Boolean: true = available, false=  unavailable
        {{
            put("06:00",true); put("06:30",true); put("07:00",true); put("07:30",true); put("08:00",true); put("08:30",true);
            put("09:00",true); put("09:30",true); put("10:00",true); put("10:30",true); put("11:00",true); put("11:30",true);
            put("12:00",true); put("12:30",true); put("13:00",true); put("13:30",true); put("14:00",true); put("14:30",true);
            put("15:00",true); put("15:30",true); put("16:00",true); put("16:30",true); put("17:00",true); put("17:30",true);
            put("18:00",true); put("18:30",true); put("19:00",true); put("19:30",true); put("20:00",true); put("20:30",true);
            put("21:00",true); put("21:30",true); put("22:00",true); put("22:30",true); put("23:00",true); put("23:30",true);
        }};

        for (ScheduleItem item : finalSchedule){
                if (item.getType().equals("task")){
                    String cat = item.getCategory();
                    Integer x = currentFreqVec.get(cat);
                    currentFreqVec.replace(cat,x,x+1);
                }
            }

            // now comparing between current and original freq vec

             for (int i = 0 ; i<9 ; i++){
                 String c = TaskCategory.fromIntToString(i).toUpperCase();
                 Integer diff = originalRequestedFreqVec.get(c) - currentFreqVec.get(c);
                 difference.put(c,diff);
             }
                Log.d("finalsch", "finalCheck: " + difference);

             // positive number - the user is missing tasks in this category - add a task from this category to the final schedule
             // negative number - the user has too much from this category - delete a task from this category to the final schedule

             //First, delete unnecessary tasks
            Iterator it1 = difference.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry pair = (Map.Entry) it1.next();
                Integer x = (Integer) pair.getValue();
                Iterator it2 = finalSchedule.iterator();
                while (x < 0 && it2.hasNext()) {
                        ScheduleItem scheduleItem = (ScheduleItem) it2.next();
                        String cat = scheduleItem.getCategory();
                        if (cat.equals(pair.getKey())) {
                            it2.remove();
                            x++;
                            pair.setValue(x);
                        }
                }
            }
            Log.d("finalsch", "finalCheck: after delete negatives = " + finalSchedule); // works
            Log.d("finalsch", "finalCheck: after delete negatives = " + difference); // works


                //  Then, if there is a positive number in "difference",
                //  for example WORK=2, try to add 2 work tasks (1 hour long each)
                //  into empty places in the final schedule (if possible)

                boolean flag=false;
                Set<String> removeSet = new HashSet<>();
                // Here we delete unavailable hours from hourslist
                Iterator it3 = finalSchedule.iterator();
                while (it3.hasNext()){
                    ScheduleItem item = (ScheduleItem) it3.next();
                    String sTime = item.getStartTime();
                    String eTime = item.getEndTime();
                    Iterator it4 = hoursMap.entrySet().iterator();
                    while (it4.hasNext()){
                        Map.Entry pair1 = (Map.Entry) it4.next();
                        String hour = (String) pair1.getKey();
                        if (hour.equals(sTime)){
                            flag=true;
                            pair1.setValue(false); // false = unavailable
                            removeSet.add((String) pair1.getKey());
                        }
                        else if (hour.equals(eTime)){
                            flag=false;
                            pair1.setValue(false); // false = unavailable
                            removeSet.add((String) pair1.getKey());
                        }
                        else if (flag==true){
                            pair1.setValue(false); // false = unavailable
                            removeSet.add((String) pair1.getKey());
                        }
                    }
                }
                hoursMap.keySet().removeAll(removeSet);
                //end of removing unavailable hours (marking them as false in the hoursmap)
                Log.d("finalsch", "finalCheck: hoursMap = " + hoursMap); // works
                onlyAvailableHoursList.addAll(hoursMap.keySet()); // all available hours are in ArrayList finalAvailableHoursList now
                List<List<String>> smallerLists = Lists.partition(onlyAvailableHoursList, 3);
                List<List<String>> smallerListsClone = new ArrayList<>(smallerLists); // clone
                Log.d("finalsch", "finalCheck: smaller Lists" + smallerLists);


                //each sublist is a possible free hour
                //continue from here

                Iterator it5 = difference.entrySet().iterator();
                while (it5.hasNext()){ // iterate on difference
                    Map.Entry pair = (Map.Entry) it5.next();
                    Integer x = (Integer) pair.getValue();
                    String cat = (String) pair.getKey();
                    Iterator it6 = smallerListsClone.iterator();
                    while (x>0 && it6.hasNext()){ // iterate until x = 0
                            List<String> smallList = (List<String>) it6.next();
                            String sTime = smallList.get(0);
                            String eTime = smallList.get(smallList.size()-1);
                            LocalTime start = LocalTime.parse(sTime);
                            LocalTime end = LocalTime.parse(eTime);
                            if (start.until(end, ChronoUnit.HOURS)==1){ // available one hour
                                ScheduleItem item = new ScheduleItem(sTime,eTime,cat,"task");
                                finalSchedule.add(item);
                                x--;
                                it6.remove();
                                pair.setValue((Integer)pair.getValue()-1);
                            }
                    }
                }
        Log.d("finalsch", "finalCheck: diffrence after one hour wiindows:" + difference);
        //half hour windows
        List<String> newAvailableHours = smallerListsClone.stream().flatMap(List::stream).collect(Collectors.toList());
        List<List<String>> newSmallerLists = Lists.partition(newAvailableHours, 2);
        List<List<String>> newSmallerListsClone = new ArrayList<>(newSmallerLists); // clone
//
//
        Iterator it7 = difference.entrySet().iterator();
        while (it7.hasNext()){ // iterate on difference
            Map.Entry pair = (Map.Entry) it7.next();
            Integer x = (Integer) pair.getValue();
            String cat = (String) pair.getKey();
            Iterator it8 = newSmallerListsClone.iterator();
            while (x>0 && it8.hasNext()){ // iterate until x = 0 or list of couples is over
                    List<String> smallList = (List<String>) it8.next();
                    String sTime = smallList.get(0);
                    String eTime = smallList.get(smallList.size()-1);
                    LocalTime start = LocalTime.parse(sTime);
                    LocalTime end = LocalTime.parse(eTime);
                    if (start.until(end, ChronoUnit.MINUTES)==30){ // available half hour
                        ScheduleItem item = new ScheduleItem(sTime,eTime,cat,"task");
                        finalSchedule.add(item);
                        it8.remove();
                        pair.setValue(x--);
                    }
                    break;
            }
        }




        Collections.sort(finalSchedule, (item1, item2) -> {
            LocalTime item1start = LocalTime.parse(item1.getStartTime());
            LocalTime item2start = LocalTime.parse(item2.getStartTime());
            if (item1start.isBefore(item2start)) return -1;
            if (item1start.isAfter(item2start)) return 1;
            else return 0;
        });

        Log.d("finalsch", "finalCheck: after final fix:" + finalSchedule);
        Log.d("finalsch", "finalCheck: after final fix:" + difference);



    }


    private void unifyGoodScheduleWithAnchors() {
        finalSchedule.addAll(recommSchGoodHours);
        //make the anchors from anchorlist a scheduleitems
        for (AnchorEntity anchor : anchorsList){
            String sTime = anchor.getStartTime();
            String eTime = anchor.getEndTime();
            String category = anchor.getCategory();
            String type = "anchor";
            String id = anchor.getAnchorID();

            ScheduleItem item = new ScheduleItem(sTime, eTime, category, type, id);
            finalSchedule.add(item);
        }
        //now sorting by start time

        Collections.sort(finalSchedule, (item1, item2) -> {
            LocalTime item1start = LocalTime.parse(item1.getStartTime());
            LocalTime item2start = LocalTime.parse(item2.getStartTime());
            if (item1start.isBefore(item2start)) return -1;
            if (item1start.isAfter(item2start)) return 1;
            else return 0;
        }); // now our schedule is sorted, trying to find ore free time windows

        Log.d("finalsch", "unifyGoodScheduleWithAnchors: " + finalSchedule);
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
                    for (int j = 0 ; j<recommSchBadHours.size() ; j++){ // small loop with index j - finding item in bad hours that matches the freq vec
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

        //now comparing again, good items with original freqvec
        Iterator<ScheduleItem> i = recommSchGoodHours.iterator();
        while(i.hasNext()) {
            ScheduleItem s = i.next();
            String category = s.getCategory();
            if (originalRequestedFreqVec.get(category) == 0) {
                i.remove();
            }
        }
    }

    private void badHoursCheck() {
        Iterator<ScheduleItem> it = recommSchBadHours.iterator();
        while (it.hasNext()) {
            String badItemCategory = it.next().getCategory();
            Integer x = requestedFreqVec.get(badItemCategory); // get the value of this category in freq vec
            if (x == 0) {    // it is not in freqvec
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
                    if (!recommSchBadHours.contains(scheduleItem)) {
                        recommSchBadHours.add(scheduleItem);
                    }
                }
                else
                { // no overlap
                    if (!recommSchGoodHours.contains(scheduleItem))
                        recommSchGoodHours.add(scheduleItem);
                }
            }
            //iterating on all goods and delete them if they are in bad
        }
        for (ScheduleItem badItem : recommSchBadHours){
            while (recommSchGoodHours.contains(badItem)){
                 recommSchGoodHours.remove(badItem);
            }
        }

    }

    private void deleteAnchorsFromRecommSch() {
        Iterator<ScheduleItem> i = recommendedSchedule.iterator();
        while (i.hasNext()) {
            ScheduleItem s = i.next(); // must be called before you can call i.remove()
            if (s.getType().equals("anchor")) {
                i.remove();
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
