package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button login;
    private TextView infoText;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //assign the variables to the id in the XML
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.loginBtn);
        avatar = (ImageView) findViewById(R.id.avatar);
        infoText = (TextView) findViewById(R.id.infoText) ;
        infoText.setText("Number of attempts remaining : 5");
        login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == login) {
                    validate(username.getText().toString(), password.getText().toString());
                }
            }


        }));
    }

    private void validate(String username, String userpassword)
    {
        System.out.println(username+" "+userpassword);
        if (username.equals("linoy18@gmail.com") && userpassword.equals("a1234")){
            Intent intent = new Intent(MainActivity.this, Homepage.class);
            startActivity(intent);
        }
    }
}
