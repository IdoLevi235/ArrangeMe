package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Homepage extends AppCompatActivity{

    private DatabaseReference mDatabase;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mButton = (Button) findViewById(R.id.test_button);



        mButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v==mButton){
                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    User user = new User("test", "test");
                    mDatabase.push().setValue(user);
                    Toast toast = Toast. makeText(getApplicationContext(),"User added",Toast. LENGTH_SHORT);
                    toast. show();

                    //User user = new User("test!!", "test!!");
                    //mDatabase.child("111").setValue(user);
                }
            }


        }));


    }


}
//