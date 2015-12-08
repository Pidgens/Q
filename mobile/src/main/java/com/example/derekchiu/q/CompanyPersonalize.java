package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
public class CompanyPersonalize extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Button next;
    String WEARABLE_COMPANY_PATH = "/wearable_company";
    GoogleApiClient googleClient;
    Bundle extras;
    TextView introText;
    DBUtil dbutil;
    String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.cp_personalize);

        extras = getIntent().getExtras();

        androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleClient.connect();

        introText = (TextView) findViewById(R.id.textView15);
        introText.setText("Hi " + extras.getString("user") + " let us know what candidates you are interested in.");


        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Intent i = new Intent(CompanyPersonalize.this, JobManage.class);
                i.putExtra("user", extras.getString("user"));


                String name = extras.getString("username");
                EditText company = (EditText) findViewById(R.id.editText);
                EditText seeking = (EditText) findViewById(R.id.cp_seek);
                i.putExtra("company", company.getText().toString());
                dbutil.saveRecruiter(androidId, name.toString(), company.getText().toString(), seeking.getText().toString());

                DataMap notifyWearable = new DataMap();
                notifyWearable.putInt("time", seconds);
                new SendToDataLayerThread(WEARABLE_COMPANY_PATH, notifyWearable).start();
                startActivity(i);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

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
