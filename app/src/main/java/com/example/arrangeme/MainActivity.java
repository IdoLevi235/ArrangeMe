package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button login;
    private TextView infoText;
    private ImageView avatar;
    private Button testButton;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //assign the variables to the id in the XML
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.loginBtn);
        avatar = (ImageView) findViewById(R.id.avatar);
        infoText = (TextView) findViewById(R.id.infoText) ;
        infoText.setText("Number of attempts remaining : 5");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("test");
        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == login) {
                    validate(username.getText().toString(), password.getText().toString());
                }

                /*if (v==testButton){
                    mDatabase.push().setValue("testttttt");
                    Toast.makeText(getApplicationContext(), "data inserted succesfully", Toast.LENGTH_LONG).show();
                    //User user = new User("test!!", "test!!");
                    //mDatabase.child("111").setValue(user);
                }*/
            }


        }));


    }

    private void validate(String username, String userpassword)
    {
        System.out.println(username+" "+userpassword);
        if (username.equals("a") && userpassword.equals("a")){
            Intent intent = new Intent(MainActivity.this, Homepage.class);
            startActivity(intent);
        }
    }

   /* public void basicReadWrite() {
        // [START write_message]
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        // [END write_message]
        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // [END read_message]
    }*/


    }
