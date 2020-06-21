package com.example.arrangeme.menu.myprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arrangeme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Class that controls achievements fragment in my profile
 */
public class Achievements extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;
    private TextView pointsToNextLevel;
    private CircularProgressBar circularProgressBar;

    /**
     *
     */
    public Achievements() {
        // Required empty public constructor
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_achievements, container, false);


    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        pointsToNextLevel=view.findViewById(R.id.TasksUntilNextLevel);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        setPointsToNextlvl();

    }

    /**
     * Setting the "points to next level" text + circular progress bar
     */
    private void setPointsToNextlvl() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("personal_info").child("points");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long p = (Long) dataSnapshot.getValue();
                if (p<200) {
                    int x = (int) (20 - (p % 20));
                    String s = String.valueOf(x);
                    pointsToNextLevel.setText(s+ " More Points Until Next level");
                    circularProgressBar.setProgress((20-x)*5);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {

    }
}
