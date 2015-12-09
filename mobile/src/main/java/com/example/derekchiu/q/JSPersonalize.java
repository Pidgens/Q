package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;




/**
 * Created by derekchiu on 11/30/15.
 */
public class JSPersonalize extends Activity {
    String androidId;
    RelativeLayout jsp_layout;
    AutoCompleteTextView school;
    AutoCompleteTextView major;
    AutoCompleteTextView positions;
    Button nextButton;
    Button addPos;
    String begin_welcome_msg = "Welcome ";
    String end_welcome_msg = ", help us let you know better by filling out this form";
    TextView welcomeTv;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_personalize);
        nextButton = (Button) findViewById(R.id.jsNext);
        welcomeTv = (TextView) findViewById(R.id.welcome);
        school = (AutoCompleteTextView) findViewById(R.id.school);
        final ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SCHOOLS);
        school.setAdapter(schoolAdapter);
        major = (AutoCompleteTextView) findViewById(R.id.major);
        String[] majors = readCSV();
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, majors);
        major.setAdapter(majorAdapter);
        positions = (AutoCompleteTextView) findViewById(R.id.position);

        major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                major.showDropDown();
            }
        });

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                school.showDropDown();
            }
        });

        school.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Arrays.asList(SCHOOLS).contains(s.toString())) {
                    nextButton.setEnabled(true);
                    nextButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                }
            }
        });



        androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String school_string = school.getText().toString();
        String major_string = major.getText().toString();
        Bundle name = getIntent().getExtras();
        String username = name.get("user").toString();
        String full_string = begin_welcome_msg + username + end_welcome_msg;
        welcomeTv.setText(full_string);
        welcomeTv.setTextColor(getResources().getColor(R.color.black));
        //dbutil.saveUser();

        final AutoCompleteTextView js_seek2 = (AutoCompleteTextView) findViewById(R.id.js_seek2);
        final AutoCompleteTextView js_seek3 = (AutoCompleteTextView) findViewById(R.id.js_seek3);
        final Button addText1 = (Button) findViewById(R.id.js_addText);
        final Button addText2 = (Button) findViewById(R.id.js_addText2);
        addText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                js_seek2.setVisibility(View.VISIBLE);
                addText2.setVisibility(View.VISIBLE);
                addText1.setVisibility(View.INVISIBLE);
            }
        });
        addText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                js_seek3.setVisibility(View.VISIBLE);
                addText2.setVisibility(View.INVISIBLE);
            }
        });

        nextButton = (Button) findViewById(R.id.jsNext);
        nextButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        nextButton.setTextColor(getResources().getColor(R.color.light_blue));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JSPersonalize.this, Manage.class);
                WearCommunicationBridge.startJSWearable(JSPersonalize.this, androidId);
                startActivity(i);
            }
        });


    }

    private static final String[] SCHOOLS = new String[] {
        "University of California, Berkeley",
        "University of California, Los Angeles",
        "University of California, Irvine",
    };

    private final String[] readCSV() {
        InputStream inputStream = getResources().openRawResource(R.raw.majors);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<String> majorAL = new ArrayList<String>();
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                majorAL.add(row[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error");
            }
        }
        return majorAL.toArray(new String[majorAL.size()]);
    };

    private static final String[] MAJORS = new String[] {

    };

}
