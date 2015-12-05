package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;


/**
 * Created by derekchiu on 11/30/15.
 */
public class JSPersonalize extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    EditText school;
    EditText major;
    EditText positions;
    Button nextButton;
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
        school = (EditText) findViewById(R.id.school);
        major = (EditText) findViewById(R.id.major);
        positions = (EditText) findViewById(R.id.position);

        String school_string = school.getText().toString();
        String major_string = major.getText().toString();
        Bundle name = getIntent().getExtras();
        String username = name.get("user").toString();
        String full_string = begin_welcome_msg + username + end_welcome_msg;
        welcomeTv.setText(full_string);

        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleClient.connect();

        nextButton = (Button) findViewById(R.id.jsNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Intent i = new Intent(JSPersonalize.this, Manage.class);
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
            Log.v("myTag", "ayy");

            PutDataMapRequest putDMR = PutDataMapRequest.create(path);
            Log.v("myTag", "ayy2");

            putDMR.getDataMap().putAll(dataMap);
            Log.v("myTag", "ayy3");
            PutDataRequest request = putDMR.asPutDataRequest();
            Log.v("myTag", "ayy4");
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient, request).await();
            Log.v("myTag", "ayy5");
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
