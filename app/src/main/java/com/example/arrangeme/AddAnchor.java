package com.example.arrangeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.arrangeme.Entities.AnchorEntity;
import com.example.arrangeme.Enums.ReminderType;
import com.google.firebase.database.DatabaseReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddAnchor extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_REQUEST_CODE = 1;
    private Button submit;
    private EditText desc;
    private Switch show_spinner;
    private Button addPhoto;
    private EditText addLocation;
    private ReminderType chosenReminder;
    private DatabaseReference mDatabase;
    private TextView textViewHelloAdd;
    private AnchorEntity anchorToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anchor);
        Toolbar toolbar = findViewById(R.id.toolbar_addTasks);
        setSupportActionBar(toolbar);
        submit = findViewById(R.id.sumbitBtn11);
        desc = findViewById(R.id.desc_text);
        addPhoto = findViewById(R.id.add_photo1);
        addPhoto.setOnClickListener(this);
        addLocation = (EditText) findViewById(R.id.locationAnc11);
        anchorToAdd = new AnchorEntity();
        textViewHelloAdd = findViewById(R.id.textViewHelloAddanchor);
        textViewHelloAdd.setText("Hello, " + Globals.currentUsername + "!");

        /* spinner stuff */
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_reminder);
        spinner.setVisibility(View.INVISIBLE);
        // Initializing a String Array
        String[] reminderItems = new String[]{
                "Select Reminder",
                "5 minutes before",
                "15 minutes before",
                "1 hour before",
                "1 day before"
        };

        final List<String> reminderItemsList = new ArrayList<>(Arrays.asList(reminderItems));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.item_spinner, reminderItemsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(desc.getHintTextColors());
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if (position > 0) {
                    // Notify the selected item text
                    chosenReminder = ReminderType.fromInt(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /* spinner stuff end */

        /* Toggle stuff */
        show_spinner = (Switch) findViewById(R.id.reminder_switch);
        show_spinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    spinner.setVisibility(View.INVISIBLE);

                }
            }
        });
        /* Toggle stuff End*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.add_photo1):
                pickFromGallery();
                break;
            default:
                break;
        }


    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Button addPhoto = (Button) findViewById(R.id.add_photo1);
                    Uri selectedImage = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable d = Drawable.createFromStream(inputStream, String.valueOf(R.drawable.add_task_round));
                        addPhoto.setHint("");
                        addPhoto.setCompoundDrawables(null, null, null, null);
                        addPhoto.setBackground(d);

                    } catch (FileNotFoundException e) {
                        Drawable d = getResources().getDrawable(R.drawable.google_xml);
                        addPhoto.setBackground(d);
                    }
                    break;
            }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_homepage, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        // Toast.makeText(AddTasks.this, "Settings clicked", Toast.LENGTH_LONG).show();
        // return true;
        //}

        return super.onOptionsItemSelected(item);
    }

}

//todo: form verifications

