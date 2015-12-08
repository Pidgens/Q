package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;




/**
 * Created by derekchiu on 11/30/15.
 */
public class JSPersonalize extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String androidId;
    RelativeLayout jsp_layout;
    AutoCompleteTextView school;
    AutoCompleteTextView major;
    AutoCompleteTextView positions;
    Button nextButton;
    Button addPos;
    String WEARABLE_JS_PATH = "/wearable_js";
    GoogleApiClient googleClient;
    String begin_welcome_msg = "Welcome ";
    String end_welcome_msg = ", help us let you know better by filling out this form";
    TextView welcomeTv;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_personalize);

        welcomeTv = (TextView) findViewById(R.id.welcome);
        school = (AutoCompleteTextView) findViewById(R.id.school);
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SCHOOLS);
        school.setAdapter(schoolAdapter);
        major = (AutoCompleteTextView) findViewById(R.id.major);
        String[] majors = readCSV();
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, majors);
        major.setAdapter(majorAdapter);
        positions = (AutoCompleteTextView) findViewById(R.id.position);

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

        jsp_layout = (RelativeLayout) findViewById(R.id.jsp_layout);
        addPos = (Button) findViewById(R.id.add_position);
        final EditText newEdit = new EditText(this);
        addPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10,200,0,0);
                newEdit.setLayoutParams(lp);
                jsp_layout.addView(newEdit);
            }
        });

        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleClient.connect();

        nextButton = (Button) findViewById(R.id.jsNext);
        nextButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        nextButton.setTextColor(getResources().getColor(R.color.light_blue));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Intent i = new Intent(JSPersonalize.this, Manage.class);
                i.putExtra("userId", androidId);
                DataMap notifyWearable = new DataMap();
                notifyWearable.putInt("time", seconds);
                new SendToDataLayerThread(WEARABLE_JS_PATH, notifyWearable).start();
                startActivity(i);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("connected", "cooL");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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

    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            // Construct a DataRequest and send over the data layer

            PutDataMapRequest putDMR = PutDataMapRequest.create(path);

            putDMR.getDataMap().putAll(dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient, request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }
        }
    }
}
